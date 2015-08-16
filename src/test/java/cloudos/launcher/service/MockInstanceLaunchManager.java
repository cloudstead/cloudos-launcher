package cloudos.launcher.service;

import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchAccount;
import org.cobbzilla.wizard.task.TaskId;

public class MockInstanceLaunchManager extends InstanceLaunchManager {

    @Override public TaskId launch(LaunchAccount account, Instance instance) {
        final InstanceStatus status = new InstanceStatus(account, instance, eventDAO);
        return taskService.execute(new MockCloudOsLaunchTask(instance, status));
    }

}
