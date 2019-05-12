package rest;

import entities.CnnVersion;
import facades.CnnVersionFacade;
import utils.RestUtils;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;

@Path("/CnnVersion")
public class CnnVersionEndpoint {
    @Inject
    CnnVersionFacade cnnVersionFacade;

    @GET
    @Path("/findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<CnnVersion> cvs = cnnVersionFacade.findAll();
        return Response.ok().entity(cnnVersionFacade.listOfObjectToJSON(cvs).toString()).build();
    }

    @GET
    @Path("/findById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        CnnVersion cv = cnnVersionFacade.findById(id);

        if(cv != null) {
            return Response.ok().entity(cv.parseToJson().toString()).build();
        } else {
            return RestUtils.createNotFoundResponse();
        }
    }

    @GET
    @Path("/findLatestVersion")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findLatestVersion() {
        CnnVersion cv = cnnVersionFacade.findLatestVersion();
        return Response.ok().entity(cv.parseToJson().toString()).build();
    }

    @GET
    @Path("downloadLatestVersion")
    @Produces("text/plain")
    public Response downloadLatestVersion() {
        CnnVersion cv = cnnVersionFacade.findLatestVersion();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("cnnVersions/" + cv.getPathToFile()).getFile());

        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=\"" + cv.getPathToFile() + "\"");
        return response.build();
    }

}
