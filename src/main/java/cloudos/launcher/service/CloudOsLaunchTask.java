package cloudos.launcher.service;

import cloudos.launcher.dao.CloudConfigDAO;
import cloudos.launcher.dao.InstanceDAO;
import cloudos.launcher.dao.LaunchConfigDAO;
import cloudos.launcher.model.Instance;
import cloudos.launcher.server.LaunchApiConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.wizard.task.TaskBase;

@Slf4j
public class CloudOsLaunchTask extends TaskBase<LauncherTaskResult> {

    @Getter protected Instance instance;
    @Getter protected InstanceStatus status;
    @Getter protected InstanceDAO instanceDAO;
    @Getter protected CloudConfigDAO cloudConfigDAO;
    @Getter protected LaunchConfigDAO launchConfigDAO;
    @Getter protected LaunchApiConfiguration configuration;

    public CloudOsLaunchTask(InstanceStatus status,
                             Instance instance,
                             InstanceDAO instanceDAO,
                             CloudConfigDAO cloudConfigDAO,
                             LaunchConfigDAO launchConfigDAO,
                             LaunchApiConfiguration configuration) {
        this.status = status;
        this.instance = instance;
        this.instanceDAO = instanceDAO;
        this.cloudConfigDAO = cloudConfigDAO;
        this.launchConfigDAO = launchConfigDAO;
        this.configuration = configuration;

        this.result.setInstance(instance);
        this.result.setStatus(this.status);
    }

    @Override public LauncherTaskResult call() throws Exception {
        new InstanceLauncher(status, instanceDAO, cloudConfigDAO, launchConfigDAO, configuration).run();
        return result;
    }

}
