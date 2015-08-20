package cloudos.launcher.main;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.model.LaunchConfig;
import lombok.Getter;
import lombok.Setter;
import org.cobbzilla.util.string.Base64;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.cobbzilla.util.daemon.ZillaRuntime.die;
import static org.cobbzilla.util.daemon.ZillaRuntime.empty;
import static org.cobbzilla.util.io.FileUtil.abs;

public class LaunchConfigOptions extends LauncherCrudOptionsBase<LaunchConfig> {

    public static final String USAGE_ZIPFILE = "The zip file. Required for create/update.";
    public static final String OPT_ZIPFILE = "-z";
    public static final String LONGOPT_ZIPFILE = "--zip-file";
    @Option(name=OPT_ZIPFILE, aliases=LONGOPT_ZIPFILE, usage=USAGE_ZIPFILE)
    @Getter @Setter private File zipFile;
    public boolean hasZipFile () { return !empty(zipFile); }

    public String getZipBase64 () {
        if (!zipFile.exists()) die("file not found: "+abs(zipFile));
        try {
            return Base64.encodeBytes(Files.readAllBytes(zipFile.toPath()));
        } catch (IOException e) {
            return die("error encoding zip file to base64: "+e, e);
        }
    }

    @Override public boolean isValidForWrite() {
        if (!hasZipFile()) required("ZIPFILE");
        return super.isValidForWrite() && hasZipFile();
    }

    @Override public String getEndpoint() { return ApiConstants.CONFIGS_ENDPOINT; }

    @Override public LaunchConfig getRequestObject() {
        return (LaunchConfig) new LaunchConfig()
                .setBase64zipData(getZipBase64())
                .setName(getName());
    }
}
