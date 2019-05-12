package rest;

import facades.UserFacade;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/auth")
public class UserEndpoint {

    @Inject
    UserFacade userFacade;

    @GET
    public String help() {
        return RootEndpoint.getResourceInformation(this.getClass());
    }

    @GET
    @Path("/get")
    public String getNewUserId() {
        return String.valueOf(userFacade.createNewUser());
    }
}
