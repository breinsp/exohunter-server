package rest;

import entities.LACandidate;
import entities.Star;
import facades.LACandidateFacade;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.RestUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/LACandidate")
public class LACandidateEndpoint {

    @Inject
    LACandidateFacade laCandidateFacade;

    @GET
    public String help() {
        return RootEndpoint.getResourceInformation(this.getClass());
    }

    @GET
    @Path("/findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<LACandidate> list = laCandidateFacade.findAll();
        JSONArray arr = laCandidateFacade.listOfObjectToJSON(list);
        return Response.ok().entity(arr.toString()).build();
    }

    @GET
    @Path("/findById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        LACandidate candidate = laCandidateFacade.findById(id);
        if (candidate != null) {
            return Response.ok().entity(candidate.parseToJson().toString()).build();
        } else {
            return RestUtils.createNotFoundResponse();
        }
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(JSONObject json) {
        Star star = Star.createFromJson(json.getJSONObject("star"));
        LACandidate candidate = LACandidate.createFromJson(json.getJSONObject("candidate"), star);
        laCandidateFacade.save(candidate);
        return RestUtils.createSingleResponse(200, "ok", "LACandidate saved!");
    }

}
