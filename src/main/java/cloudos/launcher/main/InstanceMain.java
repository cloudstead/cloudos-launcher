package cloudos.launcher.main;

import cloudos.launcher.model.support.InstanceRequest;
import cloudos.launcher.service.LauncherTaskResult;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.wizard.client.ApiClientBase;
import org.cobbzilla.wizard.task.TaskId;
import org.cobbzilla.wizard.util.RestResponse;

import static cloudos.launcher.ApiConstants.*;
import static org.cobbzilla.util.json.JsonUtil.fromJson;
import static org.cobbzilla.util.json.JsonUtil.toJson;
import static org.cobbzilla.util.system.Sleep.sleep;

@Slf4j
public class InstanceMain extends LauncherCrudMainBase<InstanceMainOptions, InstanceRequest> {

    public static void main (String[] args) { main(InstanceMain.class, args); }

    @Override protected void handleCustomAction() throws Exception {

        final InstanceMainOptions options = getOptions();
        final ApiClientBase api = getApiClient();

        if (!options.isDoLaunch() && !options.isForce() && !options.isDestroy()) {
            die("No operation selected");
        }
        if (!options.hasName()) options.requiredAndDie("NAME");

        String uri = INSTANCES_ENDPOINT + "/" + options.getName();

        if (options.isDestroy()) {
            uri += EP_DESTROY;
            out(api.doPost(uri, null));

        } else {
            uri += EP_LAUNCH + (options.isForce() ? "?force=true" : "");
            final TaskId taskId = fromJson(api.post(uri, null).json, TaskId.class);

            LauncherTaskResult result;
            sleep(options.getPollSeconds());

            final String taskUri = TASKS_ENDPOINT + "/" + taskId.getUuid();
            result = fromJson(api.get(taskUri).json, LauncherTaskResult.class);
            while (!result.isComplete()) {
                out(toJson(result));
                sleep(options.getPollSeconds());
                final RestResponse response = api.get(taskUri);
                out(response);
                if (!response.isSuccess()) {
                    log.warn("Error checking task status");
                } else {
                    result = fromJson(response.json, LauncherTaskResult.class);
                }

            }
            out(toJson(result));
        }
    }
}
