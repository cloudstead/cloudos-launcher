package cloudos.launcher.resources;

import cloudos.cslib.compute.meta.CsCloudTypeFactory;
import cloudos.launcher.ApiConstants;
import com.qmino.miredot.annotations.ReturnType;
import com.sun.jersey.api.core.HttpContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.cobbzilla.wizard.resources.ResourceUtil.ok;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(ApiConstants.CLOUD_TYPES_ENDPOINT)
@Service @Slf4j
public class CloudTypesResource {

    /**
     * Find info about all cloud types
     * @param context used to retrieve the logged-in user session
     * @return An array of CsCloudType objects
     */
    @GET
    @ReturnType("java.util.List<cloudos.cslib.compute.meta.CsCloudType>")
    public Response findAll(@Context HttpContext context) {
        return ok(CsCloudTypeFactory.Type.values());
    }

    /**
     * Find info about a single cloud type
     * @param context used to retrieve the logged-in user session
     * @param name Name of the cloud type to retrieve information for
     * @return An array of CsCloudType objects
     */
    @GET
    @Path("/{name}")
    @ReturnType("java.util.List<cloudos.cslib.compute.meta.CsCloudType>")
    public Response findByName(@Context HttpContext context,
                               @PathParam("name") String name) {
        return ok(CsCloudTypeFactory.Type.valueOf(name).getType());
    }

}