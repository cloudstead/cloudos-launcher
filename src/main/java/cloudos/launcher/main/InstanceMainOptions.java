package cloudos.launcher.main;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.model.support.InstanceRequest;
import lombok.Getter;
import lombok.Setter;
import org.cobbzilla.util.io.FileUtil;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;

public class InstanceMainOptions extends LauncherCrudOptionsBase<InstanceRequest> {

    public static final String USAGE_CLOUD = "Name of the cloud config. Required for create/update.";
    public static final String OPT_CLOUD = "-C";
    public static final String LONGOPT_CLOUD = "--cloud";
    @Option(name=OPT_CLOUD, aliases=LONGOPT_CLOUD, usage=USAGE_CLOUD)
    @Getter @Setter private String cloud;
    public boolean hasCloud () { return !empty(cloud); }

    public static final String USAGE_LAUNCH_CONFIG = "Name of the launch config. Required for create/update.";
    public static final String OPT_LAUNCH_CONFIG = "-c";
    public static final String LONGOPT_LAUNCH_CONFIG = "--config";
    @Option(name=OPT_LAUNCH_CONFIG, aliases=LONGOPT_LAUNCH_CONFIG, usage=USAGE_LAUNCH_CONFIG)
    @Getter @Setter private String launch;
    public boolean hasLaunch () { return !empty(launch); }

    public static final String USAGE_INSTANCE_TYPE = "Name of the instance type config. Required for create/update.";
    public static final String OPT_INSTANCE_TYPE = "-i";
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

    public static final String USAGE_LAUNCH = "Launch the instance. It must already exist.";
    public static final String OPT_LAUNCH = "-L";
    public static final String LONGOPT_LAUNCH = "--launch";
    @Option(name=OPT_LAUNCH, aliases=LONGOPT_LAUNCH, usage=USAGE_LAUNCH)
    @Getter @Setter private boolean doLaunch = false;

    public static final String USAGE_FORCE = "Force launch the instance. If it's already running, kill it before launching.";
    public static final String OPT_FORCE = "-f";
    public static final String LONGOPT_FORCE = "--force";
    @Option(name=OPT_FORCE, aliases=LONGOPT_FORCE, usage=USAGE_FORCE)
    @Getter @Setter private boolean force = false;

    public static final String USAGE_DESTROY = "Destroy the instance.";
    public static final String OPT_DESTROY = "-X";
    public static final String LONGOPT_DESTROY = "--destroy";
    @Option(name=OPT_DESTROY, aliases=LONGOPT_DESTROY, usage=USAGE_DESTROY)
    @Getter @Setter private boolean destroy = false;

    public static final String USAGE_POLL = "After a launch, wait this many seconds in between status checks";
    public static final String OPT_POLL = "-p";
    public static final String LONGOPT_POLL = "--poll";
    @Option(name=OPT_POLL, aliases=LONGOPT_POLL, usage=USAGE_POLL)
    @Getter @Setter private long pollSeconds = TimeUnit.SECONDS.toMillis(10);

    public static final String USAGE_IID = "Vendor-specific ID identifying the instance to launch the cloudstead on. If not set then a new instance will be launched.";
    public static final String OPT_IID = "-I";
    public static final String LONGOPT_IID = "--iid";
    @Option(name=OPT_IID, aliases=LONGOPT_IID, usage=USAGE_IID)
    @Getter @Setter private String instanceId;
    public boolean hasInstanceId () { return !empty(instanceId); }

    public static final String USAGE_SSHKEY = "Name of SSH Key to place on the instance and allow SSH access";
    public static final String OPT_SSHKEY = "-k";
    public static final String LONGOPT_SSHKEY = "--ssh-key";
    @Option(name=OPT_SSHKEY, aliases=LONGOPT_SSHKEY, usage=USAGE_SSHKEY)
    @Getter @Setter private String sshKey;
    public boolean hasSshKey () { return !empty(sshKey); }

    private static final String PK_PASSPHRASE = "PK_PASSPHRASE";
    public static final String USAGE_PRIVKEY = "Path to private SSH key to use when connecting to the instance. Put the passphrase (if any) in the "+PK_PASSPHRASE+" environment variable.";
    public static final String OPT_PRIVKEY = "-K";
    public static final String LONGOPT_PRIVKEY = "--private-key";
    @Option(name=OPT_PRIVKEY, aliases=LONGOPT_PRIVKEY, usage=USAGE_PRIVKEY)
    @Getter @Setter private File privateKey;
    public boolean hasPrivateKey () { return !empty(privateKey); }

    public String getPkPassphrase () { return System.getenv(PK_PASSPHRASE); }

    @Override public boolean isValidForWrite() {
        if (!hasCloud()) required("CLOUD");
        if (!hasLaunch()) required("LAUNCH_CONFIG");
        if (!hasInstanceType()) required("INSTANCE_TYPE");
        if (!hasRegion()) required("REGION");
        if (hasInstanceId() && !hasPrivateKey()) {
            required("PRIVKEY");
            required("USER");
        }
        return super.isValidForWrite() && hasCloud() && hasLaunch() && hasInstanceType() && hasRegion()
                && (!hasInstanceId() || (hasInstanceId() && hasPrivateKey()));
    }

    @Override public boolean isCustomAction() { return doLaunch || force || destroy; }

    @Override public String getEndpoint() { return ApiConstants.INSTANCES_ENDPOINT; }

    @Override public InstanceRequest getRequestObject() {
        return new InstanceRequest()
                .setCloud(cloud)
                .setLaunchConfig(launch)
                .setInstanceType(instanceType)
                .setRegion(region)
                .setAdditionalApps(apps)
                .setInstanceId(instanceId)
                .setSshKey(sshKey)
                .setPrivateKey(empty(privateKey) ? null : FileUtil.toStringOrDie(privateKey))
                .setKeyPassphrase(getPkPassphrase())
                .setName(getName());
    }
}
