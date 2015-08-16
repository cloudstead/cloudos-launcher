package cloudos.launcher.service;

import cloudos.dao.CloudOsEventDAO;
import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchAccount;
import cloudos.model.instance.CloudOsStatusBase;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor @Slf4j
public class InstanceStatus extends CloudOsStatusBase<LaunchAccount, Instance> {

    public InstanceStatus(LaunchAccount admin, Instance cloudOs) {
        super(admin, cloudOs);
    }

    public InstanceStatus(LaunchAccount admin, Instance cloudOs, CloudOsEventDAO eventDAO) {
        super(admin, cloudOs, eventDAO);
    }

}
