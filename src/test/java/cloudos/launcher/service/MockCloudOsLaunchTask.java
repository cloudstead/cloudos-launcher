package cloudos.launcher.service;

import cloudos.launcher.model.Instance;
import org.cobbzilla.util.system.Sleep;
import org.cobbzilla.wizard.task.TaskBase;

import java.util.concurrent.TimeUnit;

public class MockCloudOsLaunchTask extends TaskBase<LauncherTaskResult> {

    public MockCloudOsLaunchTask(Instance instance, InstanceStatus status) {
        result.setInstance(instance);
        result.setStatus(status);
    }

    @Override public LauncherTaskResult call() throws Exception {
        Sleep.sleep(TimeUnit.SECONDS.toMillis(5));
        result.getStatus().success("success");
        result.setSuccess(true);
        return result;
    }

}
