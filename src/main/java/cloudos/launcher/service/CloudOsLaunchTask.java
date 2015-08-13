package cloudos.launcher.service;

import cloudos.launcher.model.Instance;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.wizard.task.TaskBase;

@Slf4j
public class CloudOsLaunchTask extends TaskBase<LauncherTaskResult> {

    @Getter protected Instance instance;
    @Getter protected LauncherTaskResult result;

    public CloudOsLaunchTask(Instance instance) {
        this.instance = instance;
    }

    @Override
    public LauncherTaskResult call() throws Exception {
        log.info("called!");
        return result;
    }

}
