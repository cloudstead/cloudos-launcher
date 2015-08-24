package cloudos.launcher.resources;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.dao.CloudConfigDAO;
import cloudos.launcher.dao.InstanceDAO;
import cloudos.launcher.dao.LaunchConfigDAO;
import cloudos.launcher.model.CloudConfig;
import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.model.LaunchConfig;
import cloudos.launcher.model.support.InstanceRequest;
import cloudos.launcher.service.InstanceLaunchManager;
import cloudos.model.CsGeoRegion;
import cloudos.model.instance.CloudOsState;
import com.qmino.miredot.annotations.ReturnType;
import com.sun.jersey.api.core.HttpContext;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.wizard.task.TaskId;
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
    @Autowired private CloudConfigDAO cloudConfigDAO;
    @Autowired private LaunchConfigDAO launchConfigDAO;

    @Autowired private InstanceLaunchManager launchManager;

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
     * @param request Provides information required to launch the instance: name, region, additional apps
     * @return The updated instance
     */
    @POST
    @Path("/{name}")
    @ReturnType("cloudos.launcher.model.Instance")
    public Response createOrUpdate (@Context HttpContext context,
                                    @PathParam("name") String name,
                                    InstanceRequest request) {
        final LaunchAccount account = userPrincipal(context);

        final CloudConfig cloudConfig = cloudConfigDAO.findByAccountAndName(account, request.getCloud());
        if (cloudConfig == null) return invalid("err.instance.cloudConfig.notFound");

        final LaunchConfig launchConfig = launchConfigDAO.findByAccountAndName(account, request.getLaunchConfig(), false);
        if (launchConfig == null) return invalid("err.instance.launchConfig.notFound");

        Instance instance = instanceDAO.findByNameAndAccount(account, name);
        if (instance == null) {
            instance = new Instance();
            instance = populate(instance, account, request, cloudConfig, launchConfig);
            instance = instanceDAO.create(instance);

        } else {
            instance = populate(instance, account, request, cloudConfig, launchConfig);
            instance = instanceDAO.update(instance);
        }
        return ok(instance);
    }

    protected Instance populate(Instance instance,
                                LaunchAccount account,
                                InstanceRequest request,
                                CloudConfig cloudConfig,
                                LaunchConfig launchConfig) {

        final CsGeoRegion geoRegion = cloudConfig.getCloudType().getRegion(request.getRegion());
        instance.setAdminUuid(account.getUuid());
        instance.setName(request.getName());
        instance.setCloud(cloudConfig.getUuid());
        instance.setLaunch(launchConfig.getUuid());
        instance.setCsRegion(geoRegion);
        instance.setInstanceType(request.getInstanceType());
        instance.setApps(request.getAdditionalApps());
        return instance;
    }

    /**
     * Launch an instance
     * @param context used to retrieve the logged-in user session
     * @param name The name of the instance to launch
     * @param force If true, and the instance has already been launched, this will kill the running instance
     * @return a TaskId that can be used to monitor progress of the launch
     * @statuscode 422 if the instance has already been launched, and force was not true
     */
    @POST
    @Path("/{name}" + ApiConstants.EP_LAUNCH)
    @ReturnType("org.cobbzilla.wizard.task.TaskId")
    public Response launch (@Context HttpContext context,
                            @PathParam("name") String name,
                            @QueryParam("force") boolean force) {
        final LaunchAccount account = userPrincipal(context);
        final Instance found = instanceDAO.findByNameAndAccount(account, name);
        if (found == null) return notFound(name);

        if (!found.isLaunchable()) {
            if (!force) return invalid("err.instance.cannotLaunch");
            launchManager.destroy(account, found);
        }

        final TaskId taskId = launchManager.launch(account, found);
        return ok(taskId);
    }

    /**
     * Destroy an instance
     * @param context used to retrieve the logged-in user session
     * @param name The name of the instance to destroy
     * @return the state of the instance, which will be 'destroyed' if it was successfully destroyed
     */
    @POST
    @Path("/{name}" + ApiConstants.EP_DESTROY)
    @ReturnType("cloudos.model.instance.CloudOsState")
    public Response destroy (@Context HttpContext context,
                             @PathParam("name") String name) {
        final LaunchAccount account = userPrincipal(context);
        final Instance found = instanceDAO.findByNameAndAccount(account, name);
        if (found == null) return notFound(name);
        return ok(launchManager.destroy(account, found));
    }

    /**
     * Delete an instance
     * @param context used to retrieve the logged-in user session
     * @param name The name of the instance to delete. Not case-sensitive.
     * @return the state of the instance, which will be 'destroyed' if it was successfully destroyed
     * @statuscode 404 if no instance exists with that name
     */
    @DELETE
    @Path("/{name}")
    @ReturnType("cloudos.model.instance.CloudOsState")
    public Response deleteInstance (@Context HttpContext context,
                                    @PathParam("name") String name) {
        final LaunchAccount account = userPrincipal(context);
        Instance instance = instanceDAO.findByNameAndAccount(account, name);
        if (instance == null) return notFound(name);

        final CloudOsState state = launchManager.destroy(account, instance);
        instanceDAO.delete(instance.getUuid());

        return ok(state);
    }

}
