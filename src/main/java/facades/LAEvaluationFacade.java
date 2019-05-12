package facades;

import controller.CandidateCheckController;
import entities.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class LAEvaluationFacade {
    @PersistenceContext
    EntityManager em;

    @Inject
    StarFacade starFacade;
    @Inject
    UserFacade userFacade;
    @Inject
    LACandidateFacade candidateFacade;
    @Inject
    CandidateCheckController candidateCheckController;
    @Inject
    UserReportFacade userReportFacade;

    public LAEvaluation findById(Long id) {
       List<LAEvaluation> es = em.createNamedQuery("LAEvaluation.findById").setParameter("id", id).getResultList();

        if (es.size() <= 0) {
            return null;
        } else {
            return es.get(0);
        }
    }

    public List<LAEvaluation> findAll() {
        return em.createNamedQuery("LAEvaluation.findAll").getResultList();
    }

    public void save(LAEvaluation e) {
        Star star = starFacade.save(e.getStar());
        for (LACandidate c : e.getCandidates()) {
            c.setStar(star);
        }
        e.setStar(star);
        em.persist(e);
        for (LACandidate c : e.getCandidates()) {
            c.setEvaluation(e);
        }
        candidateFacade.saveRange(e.getCandidates());
    }

    public JSONArray listOfObjectToJSON(List<LAEvaluation> evals) {
        JSONArray arr = new JSONArray();
        for (LAEvaluation e : evals) {
            arr.put(e.parseToJson());
        }
        return arr;
    }

    public LAEvaluation findByStar(LAEvaluation e) {
        List<LAEvaluation> es = em.createNamedQuery("LAEvaluation.findByStar").setParameter("epic", e.getStar().getEpic()).getResultList();
        if(es.size() > 0)
            return es.get(0);
        else
            return null;
    }

    public LAEvaluation addSingleComputed(JSONObject json) {
        User user = null;
        if(json.has("userId")) {
            user = userFacade.findById(json.getLong("userId"));
        }
        LAEvaluation e = LAEvaluation.convertFromJson(json, user);
        save(e);
        candidateCheckController.checkLAEvaluation(e, true);
        return e;
    }

    public void merge(LAEvaluation e) {
        em.merge(e);
    }

    public List<LAEvaluation> findAllByStar(int epic) {
        return em.createNamedQuery("LAEvaluation.findByStar").setParameter("epic", epic).getResultList();
    }

    public LAEvaluation addSingleCustom(JSONObject json) {
        User user = null;
        if(json.has("userId")) {
            user = userFacade.findById(json.getLong("userId"));
        }
        LAEvaluation e = LAEvaluation.convertFromJson(json, user);


        List<LAEvaluation> others = em.createNamedQuery("LAEvaluation.findByUserIdAndStar").setParameter("userId", e.getUser().getId()).setParameter("epic", e.getStar().getEpic()).getResultList();
        LAEvaluation other = others.get(0);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        save(e);
        candidateCheckController.checkLAEvaluation(e, true);


        if(Math.round(e.getClassificationResult()) != Math.round(other.getClassificationResult())) {
            UserReport ur = new UserReport();
            ur.setUsed(false);
            ur.setUser(user);
            ur.setEpic(e.getStar().getEpic());
            ur.setTimeStamp(LocalDateTime.parse(LocalDateTime.now().format(dtf), dtf));
            ur.setOldClassification(other.getClassificationResult());
            ur.setUserClassification(e.getClassificationResult());

            userReportFacade.save(ur);
            userReportFacade.checkForCnnUpdate();
        }

        em.remove(other);

        return e;
    }

    public List<LACandidate> findK2LACandidateMatchingResultsOfEvals(Long id) {
        List<LACandidate> list = em.createNamedQuery("LACandidate.findCandidateMatchingByEvalId").setParameter("evalId", id).getResultList();
        return list;
    }
}
