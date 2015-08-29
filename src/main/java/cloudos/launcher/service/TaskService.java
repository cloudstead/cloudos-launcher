package cloudos.launcher.service;

import cloudos.deploy.CloudOsTaskService;
import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchAccount;
import org.springframework.stereotype.Service;

@Service public class TaskService extends CloudOsTaskService<LaunchAccount, Instance, InstanceLaunchTask, LauncherTaskResult> {}
