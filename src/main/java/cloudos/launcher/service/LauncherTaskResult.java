package cloudos.launcher.service;

import cloudos.launcher.model.Instance;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.cobbzilla.wizard.task.TaskResult;

@Accessors(chain=true)
public class LauncherTaskResult extends TaskResult {

    @Getter @Setter private Instance instance;
    @Getter @Setter private InstanceStatus status;

}
