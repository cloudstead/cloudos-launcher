package cloudos.launcher.resources;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.dao.InstanceDAO;
import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.service.TaskService;
import com.qmino.miredot.annotations.ReturnType;
import com.sun.jersey.api.core.HttpContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.cobbzilla.wizard.resources.ResourceUtil.*;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(ApiConstants.INSTANCES_ENDPOINT)
@Service @Slf4j
public class InstancesResource {

    @Autowired private InstanceDAO instanceDAO;
    @Autowired private TaskService taskService;

    /**
     * Retrieve all instances
     * @param context used to retrieve the logged-in user session
     * @return An array of Instance objects
     */
    @GET
    @ReturnType("java.util.List<cloudos.launcher.model.Instance>")
    public Response findAll (@Context HttpContext context) {
        final LaunchAccount account = userPrincipal(context);
        return ok(instanceDAO.findByAccount(account));
    }

    /**
     * Find a single instance by name
     * @param context used to retrieve the logged-in user session
     * @param name Name of the instance to read
     * @return The instance
     * @statuscode 404 no instance found with this name
     */
    @GET
    @Path("/{name}")
    @ReturnType("cloudos.launcher.model.Instance")
    public Response find (@Context HttpContext context,
                          @PathParam("name") String name) {
        final LaunchAccount account = userPrincipal(context);
        return ok(instanceDAO.findByNameAndAccount(account, name));
    }

    /**
     * Create or update an instance
     * @param context used to retrieve the logged-in user session
     * @param name The name of the instance to create or update. Not case-sensitive
     * @param instance The instance to create or update.
     * @return The updated instance
     */
    @POST
    @Path("/{name}")
    @ReturnType("cloudos.launcher.model.Instance")
    public Response createOrUpdate (@Context HttpContext context,
                                    @PathParam("name") String name,
                                    Instance instance) {
        final LaunchAccount account = userPrincipal(context);
        final Instance found = instanceDAO.findByNameAndAccount(account, name);
        if (found == null) {
            instance = instanceDAO.create(instance);

        } else {
            found.setCloud(instance.getCloud());
            found.setLaunch(instance.getLaunch());
            instance = instanceDAO.update(found);
        }
        return ok(instance);
    }

    /**
     * Delete an instance
     * @param context used to retrieve the logged-in user session
     * @param name The name of the instance to delete. Not case-sensitive.
     * @return an empty 200 OK response on success.
     * @statuscode 404 if no instance exists with that name
     */
    @DELETE
    @Path("/{name}")
    @ReturnType("java.lang.Void")
    public Response deleteInstance (@Context HttpContext context,
                                    @PathParam("name") String name) {
        final LaunchAccount account = userPrincipal(context);
        Instance instance = instanceDAO.findByNameAndAccount(account, name);
        if (instance == null) return notFound(name);

        // todo: teardown instance if it is running

        instanceDAO.delete(instance.getUuid());
        return ok();
    }

}
