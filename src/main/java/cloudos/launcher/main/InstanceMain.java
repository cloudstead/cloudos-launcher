package cloudos.launcher.main;

import cloudos.launcher.model.support.InstanceRequest;
import cloudos.launcher.service.LauncherTaskResult;
import org.cobbzilla.wizard.client.ApiClientBase;
import org.cobbzilla.wizard.task.TaskId;

import static cloudos.launcher.ApiConstants.EP_LAUNCH;
import static cloudos.launcher.ApiConstants.INSTANCES_ENDPOINT;
import static cloudos.launcher.ApiConstants.TASKS_ENDPOINT;
import static org.cobbzilla.util.json.JsonUtil.fromJson;
import static org.cobbzilla.util.json.JsonUtil.toJson;
import static org.cobbzilla.util.system.Sleep.sleep;

public class InstanceMain extends LauncherCrudMainBase<InstanceMainOptions, InstanceRequest> {

    public static void main (String[] args) { main(InstanceMain.class, args); }

    @Override protected void handleCustomAction() throws Exception {

        final InstanceMainOptions options = getOptions();
        final ApiClientBase api = getApiClient();
        if (!options.isDoLaunch() && !options.isForce()) options.requiredAndDie("LAUNCH");
        if (!options.hasName()) options.requiredAndDie("NAME");

        final String launchUri = INSTANCES_ENDPOINT
                + "/" + options.getName() + EP_LAUNCH
                + (options.isForce() ? "?force=true" : "");

        final TaskId taskId = fromJson(api.post(launchUri, null).json, TaskId.class);

        LauncherTaskResult result;
        sleep(options.getPollSeconds());

        final String taskUri = TASKS_ENDPOINT + "/" + taskId.getUuid();
        result = fromJson(api.get(taskUri).json, LauncherTaskResult.class);
        while (!result.isComplete()) {
            out(toJson(result));
            sleep(options.getPollSeconds());
            result = fromJson(api.get(taskUri).json, LauncherTaskResult.class);
        }

        out(toJson(result));
    }
}
