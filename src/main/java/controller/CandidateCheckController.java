package controller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import entities.K2Candidate;
import entities.K2LACandidateMatchingResult;
import entities.LACandidate;
import entities.LAEvaluation;
import facades.K2LACandidateMatchingResultFacade;
import facades.LACandidateFacade;
import org.json.JSONArray;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

@Stateless
public class CandidateCheckController {

    public static final double AVG_ORBITAL_PERIOD_ERROR = 0.15;
    public static final double AVG_RAD_J_ERROR = 0.25;
    public static final double AVG_EQU_ERROR = 0.15;

    @Inject
    K2LACandidateMatchingResultFacade k2LACandidateMatchingResultFacade;
    @Inject
    LACandidateFacade laCandidateFacade;

    /**
     * Check an evaluation and persist it if persistMatching is true
     * @return
     */
    public Map<String, List<K2LACandidateMatchingResult>> checkLAEvaluation(LAEvaluation e, boolean persistMatching) {
        Map<String, List<K2LACandidateMatchingResult>> res = checkForCandidates(e);
        if (persistMatching) {
            for (String k : res.keySet()) {
                for(K2LACandidateMatchingResult singleResult: res.get(k))
                    k2LACandidateMatchingResultFacade.save(singleResult);
            }
        }

        return res;
    }

    /**
     * Check if a certain Evaluation is already a K2Candidate or a confirmed K2Planet.
     * @return the matching K2Candidate or K2Planet
     */
    private static Map<String, List<K2LACandidateMatchingResult>> checkForCandidates(LAEvaluation e) {
        Client client = Client.create();
        String url = "https://exoplanetarchive.ipac.caltech.edu/cgi-bin/nstedAPI/nph-nstedAPI?" +
                "table=k2candidates&" +
                "format=json&" +
                "order=epic_name&" +
                "select=" +
                "epic_name," +
                "epic_candname," +
                "pl_name," +
                "k2c_disp," +
                "pl_orbper," +
                "pl_radj," +
                "pl_eqt," +
                "pl_fppprob&" +
                "where=epic_name='EPIC%20" + e.getStar().getEpic() + "'";
        System.out.println(url);
        WebResource resource = client.resource(url);
        ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        String str = response.getEntity(String.class);
        JSONArray arr = new JSONArray(str);
        List<K2Candidate> k2Candidates = K2Candidate.parseFromJson(arr);

        if (k2Candidates.size() <= 0) {
            return new HashMap<>();
        }

        Map<String, List<K2Candidate>> k2CandidateGroups = new HashMap<>();
        Map<String, List<K2LACandidateMatchingResult>> k2CandidateEvaluationAssociation = new HashMap<>();

        for (K2Candidate c : k2Candidates) {
            if (k2CandidateGroups.containsKey(c.getCandName())) {
                k2CandidateGroups.get(c.getCandName()).add(c);
            } else {
                k2CandidateGroups.put(c.getCandName(), new LinkedList<>(Arrays.asList(c)));
            }
        }

        for (String k : k2CandidateGroups.keySet()) {
            List<K2Candidate> curList = k2CandidateGroups.get(k);

            double avgOrbitalPeriod = 0;
            double avgRadJ = 0;
            double avgEqt = 0;

            int elOrbitalPeriod = 0;
            int elRadJ = 0;
            int elEqt = 0;

            for (K2Candidate c : curList) {
                if (c.getPlOrbitalPeriod() != null) {
                    avgOrbitalPeriod += c.getPlOrbitalPeriod();
                    elOrbitalPeriod++;
                }
                if (c.getPlRadj() != null) {
                    avgRadJ += c.getPlRadj();
                    elRadJ++;
                }
                if (c.getPlEqt() != null) {
                    avgEqt += c.getPlEqt();
                    elEqt++;
                }
            }

            if(elOrbitalPeriod > 0)
                avgOrbitalPeriod /= elOrbitalPeriod;
            if(elRadJ > 0)
                avgRadJ /= elRadJ;
            if(elEqt > 0)
                avgEqt /= elEqt;

            List<K2LACandidateMatchingResult> matchingResults = new LinkedList<>();
            List<K2LACandidateMatchingResult> backupMatchingResults = new LinkedList<>();
            for (LACandidate ec : e.getCandidates()) {
                boolean orbitalMatch = elOrbitalPeriod > 0 && ec.isPeriodValid();
                boolean radMatch = elRadJ > 0;
                boolean equMatch = elEqt > 0 && ec.isEquilibriumTempValid();
                if (elOrbitalPeriod > 0 && ec.isPeriodValid() && (ec.getPeriodAvg() < avgOrbitalPeriod - avgOrbitalPeriod * AVG_ORBITAL_PERIOD_ERROR || ec.getPeriodAvg() > avgOrbitalPeriod + avgOrbitalPeriod * AVG_ORBITAL_PERIOD_ERROR)) {
                    orbitalMatch = false;
                }
                if (elRadJ > 0 && (ec.getAvgPlanetRadius() < avgRadJ - avgRadJ * AVG_RAD_J_ERROR || ec.getAvgPlanetRadius() > avgRadJ + avgRadJ * AVG_RAD_J_ERROR)) {
                    radMatch = false;
                }
                if (elEqt > 0 && ec.isEquilibriumTempValid() && (ec.getEquTempKBB01() < avgEqt - avgEqt * AVG_EQU_ERROR || ec.getEquTempKBB01() > avgEqt + avgEqt * AVG_EQU_ERROR)) {
                    equMatch = false;
                }


                Double avgOrbitalPeriodDiff = (elOrbitalPeriod > 0) ? Math.abs(avgOrbitalPeriod - ec.getPeriodAvg()) / avgOrbitalPeriod : null;
                Double avgRadJDiff = (elRadJ > 0) ? Math.abs(avgRadJ - ec.getAvgPlanetRadius()) / avgRadJ : null;
                Double avgEqtDiff = (elEqt > 0 && ec.isEquilibriumTempValid()) ? Math.abs(avgEqt - ec.getEquTempKBB01()) / avgEqt : null;
                K2LACandidateMatchingResult matchingResult = new K2LACandidateMatchingResult(avgOrbitalPeriodDiff, avgRadJDiff, avgEqtDiff, ec);
                matchingResult.setCandidateName(k);
                matchingResult.setPlanetName(k2CandidateGroups.get(k).get(0).getPlName());
                matchingResult.setDisposition(k2CandidateGroups.get(k).get(0).getPlDisposition());
                if(orbitalMatch && radMatch || orbitalMatch && equMatch || radMatch && equMatch) {
                    matchingResults.add(matchingResult);
                } else {
                    backupMatchingResults.add(matchingResult);
                }

            }

            if(matchingResults.size() > 0) {
                k2CandidateEvaluationAssociation.put(k, matchingResults);
            } else {
                k2CandidateEvaluationAssociation.put(k, backupMatchingResults);
            }

        }

        return k2CandidateEvaluationAssociation;
    }

}
