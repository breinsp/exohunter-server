package rest;

import entities.LADataPoint;
import facades.LADataPointFacade;
import org.json.JSONArray;
import utils.RestUtils;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/LADataPoint")
public class LADataPointEndpoint {

    @Inject
    LADataPointFacade laDataPointFacade;

    @GET
    public String help() {
        return RootEndpoint.getResourceInformation(this.getClass());
    }

    @GET
    @Path("/findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<LADataPoint> list = laDataPointFacade.findAll();
        JSONArray arr = laDataPointFacade.listOfObjectToJSON(list);
        return Response.ok().entity(arr.toString()).build();
    }

    @GET
    @Path("/findById/{id}")
    public Response findById(@PathParam("id") Long id) {
        LADataPoint dp = laDataPointFacade.findById(id);
        if (dp != null) {
            return Response.ok().entity(dp.parseToJson().toString()).build();
        } else {
            return RestUtils.createNotFoundResponse();
        }
    }


}
