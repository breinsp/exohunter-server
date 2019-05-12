package rest;

import entities.LADip;
import facades.LADipFacade;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.RestUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/LADip")
public class LADipEndpoint {

    @Inject
    LADipFacade laDipFacade;

    @GET
    public String help() {
        return RootEndpoint.getResourceInformation(this.getClass());
    }

    @GET
    @Path("/findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<LADip> list = laDipFacade.findAll();
        JSONArray arr = laDipFacade.listOfObjectToJSON(list);
        return Response.ok().entity(arr.toString()).build();
    }

    @GET
    @Path("/findAll/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        LADip dip = laDipFacade.findById(id);
        if (dip != null) {
            return Response.ok().entity(dip.parseToJson().toString()).build();
        } else {
            return RestUtils.createNotFoundResponse();
        }
    }

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(JSONObject json) {
        LADip dip = LADip.createFromJson(json);
        laDipFacade.save(dip);
        return RestUtils.createSingleResponse(200, "ok", "LADip saved!");
    }

}
