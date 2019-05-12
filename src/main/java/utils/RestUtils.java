package utils;

import org.json.JSONObject;

import javax.ws.rs.core.Response;

public class RestUtils {

    public static Response createNotFoundResponse() {
        return createSingleResponse(404, "error", "Not found!");
    }

    public static Response createSingleResponse(int status, String key, Object value) {
        JSONObject obj = new JSONObject();
        obj.put(key, value);

        return Response.status(status).entity(obj.toString()).build();
    }
}
