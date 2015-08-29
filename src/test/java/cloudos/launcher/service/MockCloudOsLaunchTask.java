package cloudos.launcher.service;

import cloudos.launcher.model.Instance;
import org.cobbzilla.util.system.Sleep;

import java.util.concurrent.TimeUnit;

public class MockCloudOsLaunchTask extends InstanceLaunchTask {

    public MockCloudOsLaunchTask(Instance instance) {
        result.setCloudOs(instance);
    }

    @Override public LauncherTaskResult execute() {
        Sleep.sleep(TimeUnit.SECONDS.toMillis(5));
        result.success("success");
        return result;
    }

}
