package cloudos.launcher.service;

import cloudos.launcher.model.Instance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.wizard.task.TaskBase;

@AllArgsConstructor @Slf4j
public class CloudOsLaunchTask extends TaskBase<LauncherTaskResult> {

    @Getter protected Instance instance;

    @Override
    public LauncherTaskResult call() throws Exception {
        log.info("called!");
        return null;
    }

}
