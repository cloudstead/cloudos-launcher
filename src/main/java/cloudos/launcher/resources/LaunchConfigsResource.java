package cloudos.launcher.resources;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.dao.LaunchConfigDAO;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.model.LaunchConfig;
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
@Path(ApiConstants.CONFIGS_ENDPOINT)
@Service @Slf4j
public class LaunchConfigsResource {

    @Autowired private LaunchConfigDAO configDAO;

    /**
     * Retrieve all saved launch configs. All metadata is returned, but no zipfiles are read from storage
     * @param context used to retrieve the logged-in user session
     * @return An array of LaunchConfig objects
     */
    @GET
    @ReturnType("java.util.List<cloudos.launcher.model.LaunchConfig>")
    public Response findAll (@Context HttpContext context) {
        final LaunchAccount account = userPrincipal(context);
        return ok(configDAO.findByAccount(account));
    }

    /**
     * Find a single launch configuration by name. Zipfile data is read and returned.
     * @param context used to retrieve the logged-in user session
     * @param name Name of the config to read
     * @return The LaunchConfig including base64-encoded zipfile data
     */
    @GET
    @Path("/{name}")
    @ReturnType("cloudos.launcher.model.LaunchConfig")
    public Response find (@Context HttpContext context,
                          @PathParam("name") String name) {
        final LaunchAccount account = userPrincipal(context);
        return ok(configDAO.findByAccountAndName(account, name));
    }

    /**
     * Create or update a launch config
     * @param context used to retrieve the logged-in user session
     * @param name The name of the config to create or update. Not case-sensitive
     * @param config The config to create or update. Must contain the base64-encoded zipfile data.
     * @return The updated launch config. The zipfile data is cleared out.
     */
    @POST
    @Path("/{name}")
    @ReturnType("cloudos.launcher.model.LaunchConfig")
    public Response createOrUpdateConfig (@Context HttpContext context,
                                          @PathParam("name") String name,
                                          @Valid LaunchConfig config) {
        final LaunchAccount account = userPrincipal(context);
        final LaunchConfig found = configDAO.findByAccountAndName(account, name);
        config.setLaunchAccount(account);
        if (found == null) {
            config = configDAO.create(config);
        } else {
            found.setBase64zipData(config.getBase64zipData());
            config = configDAO.update(found);
        }

        return ok(config.clearZipData());
    }

    /**
     * Delete a launch config
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
        final LaunchConfig config = configDAO.findByAccountAndName(account, name);
        if (config == null) return notFound(name);
        configDAO.delete(config.getUuid());
        return ok();
    }
}
