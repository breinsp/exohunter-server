package rest;

import controller.CandidateCheckController;
import entities.K2LACandidateMatchingResult;
import entities.LACandidate;
import entities.LAEvaluation;
import facades.LAEvaluationFacade;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/candidateCheck")
public class LACandidateCheckEndpoint {
    
    @Inject
    LAEvaluationFacade evaluationFacade;
    @Inject
    CandidateCheckController candidateCheckController;

    @GET
    public String help() {
        return RootEndpoint.getResourceInformation(this.getClass());
    }
    
    @GET
    @Path("/check/{id}")
    public Response check(@PathParam("id") Long id) {
        LAEvaluation e = evaluationFacade.findById(id);
        Map<String, List<K2LACandidateMatchingResult>> res = candidateCheckController.checkLAEvaluation(e, false);

        StringBuilder sb = new StringBuilder();
        if (res.size() <= 0) {
            return Response.ok().entity("No K2Candidate found!").build();
        }
        for (String k : res.keySet()) {
            for(K2LACandidateMatchingResult r: res.get(k)) {
                sb.append("Found " + r.getCandidateName() + ", planet name: " + ((r.getPlanetName() != null) ? r.getPlanetName() : "undefined") + ", status: " + r.getDisposition() + "<br>");
            }
        }

        return Response.ok().entity(sb.toString()).build();
    }


}
