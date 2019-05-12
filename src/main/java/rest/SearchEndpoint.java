package rest;

import entities.LAEvaluation;
import facades.LAEvaluationFacade;
import facades.SearchFacade;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/Search")
public class SearchEndpoint {

    @Inject
    LAEvaluationFacade laEvaluationFacade;
    @Inject
    SearchFacade searchFacade;

    @POST
    @Path("/findAll")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(String json) {
        JSONObject jsonObject = new JSONObject(json);

        List<LAEvaluation> list = searchFacade.findAll(jsonObject);

        return Response.ok().entity(laEvaluationFacade.listOfObjectToJSON(list).toString()).build();
    }
}
