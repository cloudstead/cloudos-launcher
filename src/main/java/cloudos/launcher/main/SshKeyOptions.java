package cloudos.launcher.main;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.model.SshKey;
import lombok.Getter;
import lombok.Setter;
import org.cobbzilla.util.io.FileUtil;
import org.cobbzilla.wizard.api.CrudOperation;
import org.kohsuke.args4j.Option;

import java.io.File;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;

public class SshKeyOptions extends LauncherCrudOptionsBase<SshKey> {

    public static final String USAGE_KEY = "The key file. Required for create.";
    public static final String OPT_KEY = "-k";
    public static final String LONGOPT_KEY = "--ssh-key";
    @Option(name=OPT_KEY, aliases=LONGOPT_KEY, usage=USAGE_KEY)
    @Getter @Setter private File publicKey;
    public boolean hasPublicKey () { return !empty(publicKey); }

    @Override public boolean isValidForWrite() {
        required("KEY");
        return hasPublicKey() && super.isValidForWrite();
    }

    @Override public String getEndpoint() { return ApiConstants.KEYS_ENDPOINT; }

    @Override public SshKey getRequestObject() {
        return (SshKey) new SshKey()
                .setPublicKey(FileUtil.toStringOrDie(publicKey))
                .setName(getName());
    }

    // updates are not allowed
    public boolean isCustomAction() { return getOperation() == CrudOperation.update; }
}
