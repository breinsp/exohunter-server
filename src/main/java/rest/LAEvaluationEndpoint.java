package rest;

import controller.CandidateCheckController;
import entities.K2LACandidateMatchingResult;
import entities.LACandidate;
import entities.LAEvaluation;
import enums.CandidateDisposition;
import facades.*;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.RestUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("/LAEvaluation")
public class LAEvaluationEndpoint {

    @Inject
    LAEvaluationFacade laEvaluationFacade;
    @Inject
    CandidateCheckController candidateCheckController;

    @Inject
    K2LACandidateMatchingResultFacade k2LACandidateMatchingResultFacade;
    @Inject
    LACandidateFacade laCandidateFacade;
    @Inject
    UserFacade userFacade;
    @Inject
    StarFacade starFacade;
    @Inject
    LADipFacade dipFacade;

    @PersistenceContext
    EntityManager em;

    @GET
    public String help() {
        return RootEndpoint.getResourceInformation(this.getClass());
    }

    @GET
    @Path("/findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<LAEvaluation> list = laEvaluationFacade.findAll();
        JSONArray arr = laEvaluationFacade.listOfObjectToJSON(list);
        return Response.ok().entity(arr.toString()).build();
    }

    @GET
    @Path("/findById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        LAEvaluation evaluation = laEvaluationFacade.findById(id);
        if (evaluation != null) {
            return Response.ok().entity(evaluation.parseToJson().toString()).build();
        } else {
            return RestUtils.createNotFoundResponse();
        }
    }

    @POST
    @Path("/addSingleComputed")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSingleComputed(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        LAEvaluation e = laEvaluationFacade.addSingleComputed(json);
        return Response.ok().status(201).entity(e.parseToJson().toString()).build();
    }

    @POST
    @Path("/addSingleCustom")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSingleCustom(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        LAEvaluation e = laEvaluationFacade.addSingleCustom(json);
        return Response.ok().status(202).entity(e.parseToJson().toString()).build();
    }

    @GET
    @Path("/K2LACandidateMatchingResultsOfEvaluation/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findK2LACandidateMatchingResultsOfEvals(@PathParam("id") Long id) {
        List<LACandidate> candidates = laEvaluationFacade.findK2LACandidateMatchingResultsOfEvals(id);
        return Response.ok(laCandidateFacade.listOfObjectToJSON(candidates).toString()).build();
    }

    @GET
    @Path("/calcProbability/{epic}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcPropability(@PathParam("epic") int epic) {

        List<LAEvaluation> es = laEvaluationFacade.findAllByStar(epic);
        List<K2LACandidateMatchingResult> candidates = k2LACandidateMatchingResultFacade.findAllByStar(epic);

        List<String> alreadyTaken = new LinkedList<>();

        int cntOfConfirmed = 0;
        int cntOfCandidates = 0;
        int cntOfFalsePositive = 0;

        for(K2LACandidateMatchingResult c: candidates) {
            if(!alreadyTaken.contains(c.getCandidateName())) {
                if (c.getDisposition() == CandidateDisposition.CONFIRMED) {
                    cntOfConfirmed++;
                } else if (c.getDisposition() == CandidateDisposition.CANDIDATE) {
                    cntOfCandidates++;
                } else if (c.getDisposition() == CandidateDisposition.FALSE_POSITIVE) {
                    cntOfFalsePositive++;
                }
                alreadyTaken.add(c.getCandidateName());
            }
        }

        double averageClassification = 0;
        int weight = es.size();

        for(LAEvaluation e: es) {
            averageClassification += e.getClassificationResult() * weight;
            weight--;
        }
        int div = 0;
        for(int i = es.size(); i >= 0; i--) {
            div += i;
        }
        averageClassification /= div;

        int numberOfEvaluations = es.size();

        JSONObject json = new JSONObject();
        json.put("cntOfEvaluations", numberOfEvaluations);
        json.put("cntOfConfirmed", cntOfConfirmed);
        json.put("cntOfCandidates", cntOfCandidates);
        json.put("cntOfFalsePositive", cntOfFalsePositive);
        json.put("averageClassification", averageClassification);
        json.put("star", es.get(0).getStar().parseToJson());
        json.put("distinctCandidateNames", alreadyTaken);

        JSONArray candidateArr = new JSONArray();
        for(LACandidate c:es.get(0).getCandidates()) {
            candidateArr.put(c.parseToJson());
        }
        json.put("candidates", candidateArr);

        JSONArray evalArr = new JSONArray();
        for(LAEvaluation e: es) {
            e.setCandidates(new LinkedList<>());
            e.setStar(null);
            evalArr.put(e.parseToJson());
        }
        json.put("evaluations", evalArr);

        return Response.ok().entity(json.toString()).build();
    }

}
