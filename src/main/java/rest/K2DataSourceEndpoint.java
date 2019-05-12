package rest;

import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import controller.DistributionController;
import controller.K2DataController;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.RestUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/k2")
public class K2DataSourceEndpoint {

    //201071583

    @GET
    public String help() {
        return RootEndpoint.getResourceInformation(this.getClass());
    }

    @GET
    @Path("/debug")
    public String debug() {
        return "Debug";
    }

    @GET
    @Path("/fetchCampaign/{nr}")
    @Produces("application/json")
    public Response fetchCampaign(@PathParam("nr") String epic) {
        int campaign = -1;
        try {
            campaign = K2DataController.getCampaignForEpicNr(Integer.parseInt(epic));
        } catch (Exception ex) {
            return Response.serverError().entity(ex.toString()).build();
        }
        JsonObject result = new JsonObject();
        result.addProperty("campaign", campaign);

        return Response.ok().entity(result.toString()).build();
    }

    /*
    @GET
    @Path("/fetchLightcurve/{nr}")
    @Produces("application/json")
    public Response fetchLightcurve(@PathParam("nr") String epic) {
        try {
            int epicNr = Integer.parseInt(epic);
            int campaign = K2DataController.getCampaignForEpicNr(epicNr);

            LCData lcData = K2DataController.getLightCurveForStar(epicNr, campaign);

            JsonArray x = (JsonArray) new Gson().toJsonTree(lcData.getxAxis(),
                    new TypeToken<List<Double>>() {
                    }.getType());
            JsonArray y = (JsonArray) new Gson().toJsonTree(lcData.getyAxis(),
                    new TypeToken<List<Double>>() {
                    }.getType());

            JsonObject result = new JsonObject();
            result.addProperty("epicNr", lcData.getEpicNr());

            result.addProperty("xMin", lcData.getxMin());
            result.addProperty("xMax", lcData.getxMax());
            result.addProperty("yMin", lcData.getyMin());
            result.addProperty("yMax", lcData.getyMax());

            result.add("xAxis", x);
            result.add("yAxis", y);


            return Response.ok().entity(result.toString()).build();

        } catch (Exception ex) {
            JsonObject err = new JsonObject();
            err.addProperty("ERROR", ex.toString());
            return Response.serverError().entity(err.toString()).build();
        }
    }*/

    @GET
    @Path("/fetchLightcurveURL/{nr}")
    public String fetchLightcurveURL(@PathParam("nr") String nr) {
        try {
            int epicNr = Integer.parseInt(nr);
            int campaign = K2DataController.getCampaignForEpicNr(epicNr);
            return K2DataController.getLightCurveURL(epicNr, campaign);
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    @GET
    @Path("/fetchFullLightcurve/{nr}")
    public Response fetchFullLightCurve(@PathParam("nr") String nr) {
        try {
            int epicNr = Integer.parseInt(nr);
            int campaign = K2DataController.getCampaignForEpicNr(epicNr);
            String url = K2DataController.getLightCurveURL(epicNr, campaign);

            Client client = Client.create();
            System.out.println(url);
            WebResource resource = client.resource(url);
            ClientResponse response = resource.accept("application/text").get(ClientResponse.class);
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }
            String str = response.getEntity(String.class);
            String[] lines = str.split("\n");

            JSONArray arr = new JSONArray();
            for (int i = 1; i < lines.length; i++) {
                String[] parts = lines[i].split("[,]");
                JSONObject json = new JSONObject();
                json.put("x", Float.parseFloat(parts[0]));
                json.put("y", Float.parseFloat(parts[1]));
                arr.put(json);
            }
            return Response.ok().entity(arr.toString()).build();
        } catch (Exception ex) {
            return RestUtils.createNotFoundResponse();
        }
    }

    private final String[] predefinedValues = new String[]{
            "202095376", "202135709", "202085143", "203771098", "202091934", "202086694"
    };


    @GET
    @Path("/getK2Id")
    public String getK2Id() {
        DistributionController distributionController = DistributionController.getInstance();
        System.out.println("Cache Size: " + distributionController.getCache().size());
        return distributionController.getValue();


        /*
        String val = predefinedValues[Test.count];
        Test.count += 1;
        if (Test.count == predefinedValues.length) {
            Test.count = 0;
        }
        return val;*/
    }

    @GET
    @Path("/getK2IdFromAlias/{alias}")
    public String getK2IdFromAlias(@PathParam("alias") String alias) {
        return K2DataController.getEpicFromAlias(alias);
    }

    public static class Test {
        public static int count = 0;
    }
}
