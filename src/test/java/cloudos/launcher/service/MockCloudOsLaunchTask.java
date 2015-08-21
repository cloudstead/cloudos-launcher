package cloudos.launcher.service;

import cloudos.launcher.model.Instance;
import org.cobbzilla.util.system.Sleep;
import org.cobbzilla.wizard.task.TaskBase;

import java.util.concurrent.TimeUnit;

public class MockCloudOsLaunchTask extends TaskBase<LauncherTaskResult> {

    public MockCloudOsLaunchTask(Instance instance) {
        result.setCloudOs(instance);
    }

    @Override public LauncherTaskResult call() throws Exception {
        Sleep.sleep(TimeUnit.SECONDS.toMillis(5));
        result.success("success");
        return result;
    }

}
