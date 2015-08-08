package cloudos.launcher.resources;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.dao.CloudConfigDAO;
import cloudos.launcher.model.CloudConfig;
import cloudos.launcher.model.LaunchAccount;
import com.qmino.miredot.annotations.ReturnType;
import com.sun.jersey.api.core.HttpContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.cobbzilla.wizard.resources.ResourceUtil.notFound;
import static org.cobbzilla.wizard.resources.ResourceUtil.ok;
import static org.cobbzilla.wizard.resources.ResourceUtil.userPrincipal;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(ApiConstants.CLOUDS_ENDPOINT)
@Service @Slf4j
public class CloudConfigsResource {

    @Autowired private CloudConfigDAO configDAO;

    /**
     * Retrieve all saved cloud configs. All metadata is returned, but no zipfiles are read from storage
     * @param context used to retrieve the logged-in user session
     * @return An array of CloudConfig objects
     */
    @GET
    @ReturnType("java.util.List<cloudos.launcher.model.CloudConfig>")
    public Response findAll (@Context HttpContext context) {
        final LaunchAccount account = userPrincipal(context);
        return ok(configDAO.findByAccount(account));
    }

    /**
     * Find a single launch configuration by name. Zipfile data is read and returned.
     * @param context used to retrieve the logged-in user session
     * @param name Name of the config to read
     * @return The CloudConfig including base64-encoded zipfile data
     */
    @GET
    @Path("/{name}")
    @ReturnType("cloudos.launcher.model.CloudConfig")
    public Response find (@Context HttpContext context,
                          @PathParam("name") String name) {
        final LaunchAccount account = userPrincipal(context);
        return ok(configDAO.findByAccountAndName(account, name));
    }

    /**
     * Create or update a cloud config
     * @param context used to retrieve the logged-in user session
     * @param name The name of the config to create or update. Not case-sensitive
     * @param config The config to create or update
     * @return The updated cloud config
     */
    @POST
    @Path("/{name}")
    @ReturnType("cloudos.launcher.model.CloudConfig")
    public Response createOrUpdateConfig (@Context HttpContext context,
                                          @PathParam("name") String name,
                                          @Valid CloudConfig config) {
        final LaunchAccount account = userPrincipal(context);
        final CloudConfig found = configDAO.findByAccountAndName(account, name);
        config.setLaunchAccount(account);
        if (found == null) {
            config = configDAO.create(config);
        } else {
            config = configDAO.update(found);
        }
        return ok(config.setLaunchAccount(account).decrypt());
    }

    /**
     * Delete a cloud config
     * @param context used to retrieve the logged-in user session
     * @param name The name of the config to delete. Not case-sensitive.
     * @return an empty 200 OK response on success.
     * @statuscode 404 if no config exists with that name
     */
    @DELETE
    @Path("/{name}")
    @ReturnType("java.lang.Void")
    public Response deleteConfig (@Context HttpContext context,
                                  @PathParam("name") String name) {
        final LaunchAccount account = userPrincipal(context);
        CloudConfig config = configDAO.findByAccountAndName(account, name);
        if (config == null) return notFound(name);
        configDAO.delete(config.getUuid());
        return ok();
    }
}
