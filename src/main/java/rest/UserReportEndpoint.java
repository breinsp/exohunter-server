package rest;

import entities.User;
import entities.UserReport;
import facades.UserFacade;
import facades.UserReportFacade;
import org.json.JSONObject;
import utils.RestUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/UserReport")
public class UserReportEndpoint {

    @Inject
    UserFacade userFacade;
    @Inject
    UserReportFacade userReportFacade;

    //save endpoint currently not in use
    public Response save(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        User user = null;
        if(json.has("userId"))
            user = userFacade.findById(json.getLong("userId"));
        UserReport ur = UserReport.parseJsonToObject(json, user);
        userReportFacade.save(ur);
        userReportFacade.checkForCnnUpdate();
        return Response.ok().entity(ur.parseToJson().toString()).build();
    }

    @GET
    @Path("/findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        return Response.ok().entity(userReportFacade.listOfObjectToJSON(userReportFacade.findAll()).toString()).build();
    }

    @GET
    @Path("/findById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        UserReport ur = userReportFacade.findById(id);
        if (ur != null) {
            return Response.ok().entity(ur.parseToJson().toString()).build();
        } else {
            return RestUtils.createNotFoundResponse();
        }
    }

    @GET
    @Path("/findByStar/{epic}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByStar(@PathParam("epic") int epic) {
        List<UserReport> urs = userReportFacade.findByStar(epic);
        return Response.ok().entity(userReportFacade.listOfObjectToJSON(urs).toString()).build();
    }

    @GET
    @Path("/getCntOfStarUserReports/{epic}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCntOfStarUserReports(@PathParam("epic") int epic) {
        int cnt = userReportFacade.getCntOfStarUserReports(epic);
        return RestUtils.createSingleResponse(200, "cnt", cnt);
    }
}
