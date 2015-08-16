package cloudos.launcher.main;

import lombok.Getter;
import lombok.Setter;
import org.kohsuke.args4j.Option;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;

public class CloudTypeOptions extends LauncherMainOptionsBase {

    public static final String USAGE_VENDOR = "Name of the cloud vendor.";
    public static final String OPT_VENDOR = "-v";
    public static final String LONGOPT_VENDOR = "--vendor";
    @Option(name=OPT_VENDOR, aliases=LONGOPT_VENDOR, usage=USAGE_VENDOR)
    @Getter @Setter private String vendor;
    public boolean hasVendor () { return !empty(vendor); }

}
