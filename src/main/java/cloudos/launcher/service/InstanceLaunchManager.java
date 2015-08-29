package cloudos.launcher.service;

import cloudos.deploy.LaunchManagerBase;
import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.server.LaunchApiConfiguration;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstanceLaunchManager extends LaunchManagerBase<LaunchAccount, Instance, LauncherTaskResult, InstanceLaunchTask> {

    @Autowired @Getter private LaunchApiConfiguration configuration;

    @Override protected InstanceLaunchTask launchTask(LaunchAccount account, Instance instance) {
        instance.setLaunchAccount(account);
        return super.launchTask(account, instance);
    }

}
