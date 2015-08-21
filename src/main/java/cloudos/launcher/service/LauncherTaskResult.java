package cloudos.launcher.service;

import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchAccount;
import cloudos.model.instance.CloudOsEvent;
import cloudos.model.instance.CloudOsTaskResultBase;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cobbzilla.wizard.dao.DAO;

@Accessors(chain=true) @NoArgsConstructor
public class LauncherTaskResult extends CloudOsTaskResultBase<LaunchAccount, Instance> {

    public LauncherTaskResult(LaunchAccount admin, Instance cloudOs) { super(admin, cloudOs); }

    public LauncherTaskResult(LaunchAccount admin, Instance cloudOs, DAO<CloudOsEvent> eventDAO) {
        super(admin, cloudOs, eventDAO);
    }

}
