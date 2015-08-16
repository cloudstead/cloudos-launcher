package cloudos.launcher;

import cloudos.cslib.compute.CsCloudFactory;
import cloudos.cslib.compute.meta.CsCloudTypeFactory;

public class ApiConstants {

    public static final String H_TOKEN = "__launcher_session";

    public static final String AUTH_ENDPOINT = "/auth";
    public static final String ACCOUNTS_ENDPOINT = "/accounts";
    public static final String CONFIGS_ENDPOINT = "/configs";
    public static final String CLOUDS_ENDPOINT = "/clouds";
    public static final String INSTANCES_ENDPOINT = "/instances";
    public static final String TASKS_ENDPOINT = "/tasks";
    public static final String CLOUD_TYPES_ENDPOINT = "/cloud_types";

    // account endpoints
    public static final String EP_CHANGE_PASSWORD = "/change_password";

    // factories
    public static final CsCloudTypeFactory CLOUD_TYPE_FACTORY = new CsCloudTypeFactory();
    public static final CsCloudFactory CLOUD_FACTORY = new CsCloudFactory();

}
