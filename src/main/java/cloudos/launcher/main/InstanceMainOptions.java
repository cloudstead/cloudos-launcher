package cloudos.launcher.main;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.model.support.InstanceRequest;
import lombok.Getter;
import lombok.Setter;
import org.kohsuke.args4j.Option;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;

public class InstanceMainOptions extends LauncherCrudOptionsBase<InstanceRequest> {

    public static final String USAGE_CLOUD = "Name of the cloud config. Required for create/update.";
    public static final String OPT_CLOUD = "-c";
    public static final String LONGOPT_CLOUD = "--cloud";
    @Option(name=OPT_CLOUD, aliases=LONGOPT_CLOUD, usage=USAGE_CLOUD)
    @Getter @Setter private String cloud;
    public boolean hasCloud () { return !empty(cloud); }

    public static final String USAGE_LAUNCH = "Name of the launch config. Required for create/update.";
    public static final String OPT_LAUNCH = "-L";
    public static final String LONGOPT_LAUNCH = "--launch";
    @Option(name=OPT_LAUNCH, aliases=LONGOPT_LAUNCH, usage=USAGE_LAUNCH)
    @Getter @Setter private String launch;
    public boolean hasLaunch () { return !empty(launch); }

    public static final String USAGE_INSTANCE_TYPE = "Name of the instance type config. Required for create/update.";
    public static final String OPT_INSTANCE_TYPE = "-I";
    public static final String LONGOPT_INSTANCE_TYPE = "--instance-type";
    @Option(name=OPT_INSTANCE_TYPE, aliases=LONGOPT_INSTANCE_TYPE, usage=USAGE_INSTANCE_TYPE)
    @Getter @Setter private String instanceType;
    public boolean hasInstanceType () { return !empty(instanceType); }

    public static final String USAGE_REGION = "Name of the instance type config. Required for create/update.";
    public static final String OPT_REGION = "-r";
    public static final String LONGOPT_REGION = "--region";
    @Option(name=OPT_REGION, aliases=LONGOPT_REGION, usage=USAGE_REGION)
    @Getter @Setter private String region;
    public boolean hasRegion () { return !empty(region); }

    public static final String USAGE_APPS = "Additional apps to install.";
    public static final String OPT_APPS = "-A";
    public static final String LONGOPT_APPS = "--apps";
    @Option(name=OPT_APPS, aliases=LONGOPT_APPS, usage=USAGE_APPS)
    @Getter @Setter private String apps;
    public boolean hasApps () { return !empty(apps); }

    @Override public boolean isValidForWrite() {
        return super.isValidForWrite() && hasCloud() && hasLaunch() && hasInstanceType() && hasRegion();
    }

    @Override public String getEndpoint() { return ApiConstants.INSTANCES_ENDPOINT; }

    @Override public InstanceRequest getRequestObject() {
        return new InstanceRequest()
                .setCloud(cloud)
                .setLaunchConfig(launch)
                .setInstanceType(instanceType)
                .setRegion(region)
                .setAdditionalApps(apps)
                .setName(getName());
    }
}
