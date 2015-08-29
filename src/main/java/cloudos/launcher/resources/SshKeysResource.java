package cloudos.launcher.resources;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.dao.SshKeyDAO;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.model.SshKey;
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

import static org.cobbzilla.wizard.resources.ResourceUtil.*;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(ApiConstants.KEYS_ENDPOINT)
@Service @Slf4j
public class SshKeysResource {

    @Autowired private SshKeyDAO keyDAO;

    /**
     * Retrieve all keys
     * @param context used to retrieve the logged-in user session
     * @return An array of SshKey objects
     */
    @GET
    @ReturnType("java.util.List<cloudos.launcher.model.SshKey>")
    public Response findAll (@Context HttpContext context) {
        final LaunchAccount account = userPrincipal(context);
        return ok(keyDAO.findByAccount(account));
    }

    /**
     * Find a single key by name
     * @param context used to retrieve the logged-in user session
     * @param name Name of the key to read
     * @return The key found
     */
    @GET
    @Path("/{name}")
    @ReturnType("cloudos.launcher.model.SshKey")
    public Response find (@Context HttpContext context,
                          @PathParam("name") String name) {
        final LaunchAccount account = userPrincipal(context);
        final SshKey key = keyDAO.findByAccountAndName(account, name);
        return key == null ? notFound(name) : ok(key);
    }

    /**
     * Create a key. Keys cannot be updated, please delete first before re-creating.
     * @param context used to retrieve the logged-in user session
     * @param name The name of the key to create or update. Not case-sensitive
     * @param key The key to create or update
     * @return The updated cloud key
     */
    @POST
    @Path("/{name}")
    @ReturnType("cloudos.launcher.model.SshKey")
    public Response createKey(@Context HttpContext context,
                              @PathParam("name") String name,
                              @Valid SshKey key) {
        final LaunchAccount account = userPrincipal(context);
        final SshKey found = keyDAO.findByAccountAndName(account, name);
        if (found == null) {
            key = keyDAO.create(key.setAccount(account.getUuid()));
        } else {
            return invalid("err.sshkey.cannotUpdate");
        }
        return ok(key);
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
    public Response deleteKey(@Context HttpContext context,
                              @PathParam("name") String name) {
        final LaunchAccount account = userPrincipal(context);
        SshKey config = keyDAO.findByAccountAndName(account, name);
        if (config == null) return notFound(name);
        keyDAO.delete(config.getUuid());
        return ok();
    }

}
