package rest;

import entities.Star;
import facades.StarFacade;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.RestUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/Star")
public class StarEndpoint {

    @Inject
    StarFacade starFacade;

    @GET
    public String help() {
        return RootEndpoint.getResourceInformation(this.getClass());
    }

    @GET
    @Path("/findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<Star> list = starFacade.findAll();
        JSONArray arr = starFacade.listOfObjectToJSON(list);
        return Response.ok().entity(arr.toString()).build();
    }

    @GET
    @Path("/findById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        Star star = starFacade.findById(id);
        if (star != null) {
            return Response.ok().entity(star.parseToJson().toString()).build();
        } else {
            return RestUtils.createNotFoundResponse();
        }
    }

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(JSONObject json) {
        Star star = Star.createFromJson(json);
        starFacade.save(star);
        return RestUtils.createSingleResponse(200, "ok", "Star saved!");
    }
}
