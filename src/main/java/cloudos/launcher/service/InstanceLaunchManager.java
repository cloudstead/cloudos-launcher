package cloudos.launcher.service;

import cloudos.dao.CloudOsEventDAO;
import cloudos.launcher.dao.CloudConfigDAO;
import cloudos.launcher.dao.InstanceDAO;
import cloudos.launcher.dao.LaunchConfigDAO;
import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.server.LaunchApiConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.wizard.task.TaskId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class InstanceLaunchManager {

    @Autowired protected CloudOsEventDAO eventDAO;
    @Autowired protected InstanceDAO instanceDAO;
    @Autowired protected CloudConfigDAO cloudConfigDAO;
    @Autowired protected LaunchConfigDAO launchConfigDAO;
    @Autowired protected LaunchApiConfiguration configuration;
    @Autowired protected TaskService taskService;

    public TaskId launch(LaunchAccount account, Instance instance) {
        final InstanceStatus status = new InstanceStatus(account, instance, eventDAO);
        final CloudOsLaunchTask task = new CloudOsLaunchTask(status, instance, instanceDAO, cloudConfigDAO, launchConfigDAO, configuration);
        return taskService.execute(task);
    }

}
