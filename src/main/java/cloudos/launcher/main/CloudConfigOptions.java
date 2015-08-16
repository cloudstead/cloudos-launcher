package cloudos.launcher.main;

import cloudos.cslib.compute.meta.CsCloudTypeFactory;
import cloudos.launcher.ApiConstants;
import cloudos.launcher.model.CloudConfig;
import lombok.Getter;
import lombok.Setter;
import org.kohsuke.args4j.Option;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;

public class CloudConfigOptions extends LauncherCrudOptionsBase<CloudConfig> {

    public static final String USAGE_VENDOR = "The cloud vendor. Required for create/update.";
    public static final String OPT_VENDOR = "-V";
    public static final String LONGOPT_VENDOR = "--vendor";
    @Option(name=OPT_VENDOR, aliases=LONGOPT_VENDOR, usage=USAGE_VENDOR)
    @Getter @Setter private CsCloudTypeFactory.Type vendor;
    public boolean hasVendor () { return !empty(vendor); }

    public static final String USAGE_ACCESS_KEY = "The access key. Required for create/update.";
    public static final String OPT_ACCESS_KEY = "-A";
    public static final String LONGOPT_ACCESS_KEY = "--access-key";
    @Option(name=OPT_ACCESS_KEY, aliases=LONGOPT_ACCESS_KEY, usage=USAGE_ACCESS_KEY)
    @Getter @Setter private String accessKey;
    public boolean hasAccessKey () { return !empty(accessKey); }

    public static final String CL_CLOUD_SECRET = "CL_CLOUD_SECRET";

    public static final String USAGE_SECRET_KEY = "The secret key. Required for create/update. Prefix with @ to read from env var. Default is "+CL_CLOUD_SECRET;
    public static final String OPT_SECRET_KEY = "-S";
    public static final String LONGOPT_SECRET_KEY = "--secret-key";
    @Option(name=OPT_SECRET_KEY, aliases=LONGOPT_SECRET_KEY, usage=USAGE_SECRET_KEY)
    @Setter private String secretKey = "@" + CL_CLOUD_SECRET;
    public String getSecretKey() {
        return secretKey.startsWith("@") ? System.getenv(secretKey) : secretKey;
    }
    public boolean hasSecretKey () { return !empty(getSecretKey()); }

    public static final String USAGE_OPTIONAL_JSON = "Optional additional JSON config for the cloud.";
    public static final String OPT_OPTIONAL_JSON = "-O";
    public static final String LONGOPT_OPTIONAL_JSON = "--optional-json";
    @Option(name=OPT_OPTIONAL_JSON, aliases=LONGOPT_OPTIONAL_JSON, usage=USAGE_OPTIONAL_JSON)
    @Getter @Setter private String optionalJson;

    @Override public String getEndpoint() { return ApiConstants.CLOUDS_ENDPOINT; }

    @Override public boolean isValidForWrite () {
        return super.isValidForWrite() && hasVendor() && hasAccessKey() && hasSecretKey();
    }

    @Override public CloudConfig getRequestObject() {
        return (CloudConfig) new CloudConfig()
                .setVendor(getVendor().getType().getProviderName())
                .setAccessKey(getAccessKey())
                .setSecretKey(getSecretKey())
                .setOptionalJson(getOptionalJson())
                .setName(getName());
    }

}
