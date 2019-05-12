package rest;

import entities.ExoplanetStat;
import facades.ExoplanetStatFacade;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.RestUtils;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ExoplanetStats")
public class ExoplanetStatEndpoint {

    @Inject
    ExoplanetStatFacade exoplanetStatFacade;

    @GET
    @Path("/findCurrentExoplanetStat")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCurrentExoplanetStat() {
        ExoplanetStat es = exoplanetStatFacade.findLatestExoplanetStatAndUpdate();
        return Response.ok().entity(es.parseToJson().toString()).build();
    }

    @GET
    @Path("/findCntOfConfirmed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCntOfConfirmed() {
        ExoplanetStat es = exoplanetStatFacade.findLatestExoplanetStatAndUpdate();
        int count = exoplanetStatFacade.findCntOfConfirmed(es);
        return RestUtils.createSingleResponse(200, "count", count);
    }

    @GET
    @Path("/findCntPerYear")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCntPerYear() {
        ExoplanetStat es = exoplanetStatFacade.findLatestExoplanetStatAndUpdate();
        JSONArray arr = exoplanetStatFacade.findCntPerYear(es);
        return Response.ok().entity(arr.toString()).build();
    }


    @GET
    @Path("/findCntPerYearAndMethod")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCntPerYearAndMethod() {
        ExoplanetStat es = exoplanetStatFacade.findLatestExoplanetStatAndUpdate();
        JSONArray arr = exoplanetStatFacade.findCntPerYearAndMethod(es);
        return Response.ok().entity(arr.toString()).build();
    }

    @GET
    @Path("/findCntPerYearAndMethodPerc")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCntPerYearAndMethodCnt() {
        ExoplanetStat es = exoplanetStatFacade.findLatestExoplanetStatAndUpdate();
        JSONObject json = exoplanetStatFacade.findCntPerYearAndMethodPerc(es);
        return Response.ok().entity(json.toString()).build();
    }
}
