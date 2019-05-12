package rest;

import entities.K2LACandidateMatchingResult;
import facades.K2LACandidateMatchingResultFacade;
import org.json.JSONArray;
import utils.RestUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/K2LACandidateMatchingResult")
public class K2LACandidateMatchingResultEndpoint {

    @Inject
    K2LACandidateMatchingResultFacade k2LACandidateMatchingResultFacade;

    @GET
    public String help() {
        return RootEndpoint.getResourceInformation(this.getClass());
    }

    @GET
    @Path("/findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<K2LACandidateMatchingResult> list = k2LACandidateMatchingResultFacade.findAll();
        JSONArray arr = k2LACandidateMatchingResultFacade.listOfObjectToJSON(list);
        return Response.ok().entity(arr.toString()).build();
    }

    @GET
    @Path("/findById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        K2LACandidateMatchingResult c = k2LACandidateMatchingResultFacade.findById(id);
        if (c != null) {
            return Response.ok().entity(c.parseToJson().toString()).build();
        } else {
            return RestUtils.createNotFoundResponse();
        }
    }

}
