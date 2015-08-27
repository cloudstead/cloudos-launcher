package cloudos.launcher.dao;

import cloudos.launcher.model.LaunchConfig;

public class MockLaunchConfigDAO extends LaunchConfigDAO {

    @Override protected void validate(LaunchConfig config) {
        // skip certificate validation
        config.writeZipData();
    }

}
