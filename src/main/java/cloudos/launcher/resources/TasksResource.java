package cloudos.launcher.resources;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.service.LauncherTaskResult;
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
@Path(ApiConstants.TASKS_ENDPOINT)
@Service @Slf4j
public class TasksResource {

    @Autowired private TaskService taskService;

    /**
     * Retrieve history for a background task
     * @param context used to retrieve the logged-in user session
     * @param uuid The background task to look up
     * @return a TaskResult representing the history for the task
     */
    @GET
    @Path("/{uuid}")
    @ReturnType("cloudos.launcher.service.LauncherTaskResult")
    public Response getHistory (@Context HttpContext context,
                                @PathParam("uuid") String uuid) {

        final LaunchAccount account = userPrincipal(context);
        final LauncherTaskResult result = taskService.getResult(uuid);

        if (result == null) return notFound(uuid);
        if (!account.getUuid().equals(result.getInstance().getAdminUuid())) return forbidden();
        return ok(result);
    }

}
