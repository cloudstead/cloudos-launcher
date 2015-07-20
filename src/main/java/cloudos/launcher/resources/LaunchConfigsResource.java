package cloudos.launcher.resources;

import cloudos.launcher.server.LaunchApiConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(LaunchConfigsResource.CONFIGS_ENDPOINT)
@Service @Slf4j
public class LaunchConfigsResource {

    public static final String CONFIGS_ENDPOINT = "/configs";

    @Autowired private LaunchApiConfiguration configuration;

    @GET
    public Response test () {
        return Response.ok("we got here!").build();
    }

}
