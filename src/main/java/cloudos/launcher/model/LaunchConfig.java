package cloudos.launcher.model;

import cloudos.launcher.server.LaunchApiConfiguration;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.util.security.Crypto;
import org.cobbzilla.util.string.Base64;
import org.cobbzilla.wizard.model.UniquelyNamedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.*;

import static org.cobbzilla.util.daemon.ZillaRuntime.die;
import static org.cobbzilla.util.daemon.ZillaRuntime.empty;

@Entity @Slf4j @Accessors(chain=true)
public class LaunchConfig extends UniquelyNamedEntity {

    @Column(nullable=false, length=UUID_MAXLEN)
    @Getter @Setter private String account;

    @Transient @JsonIgnore
    @Getter private LaunchAccount launchAccount;
    public LaunchConfig setLaunchAccount(LaunchAccount launchAccount) {
        this.launchAccount = launchAccount;
        this.account = launchAccount == null ? null : launchAccount.getUuid();
        return this;
    }

    @Transient
    @Getter @Setter private String base64zipData;
    public boolean hasBase64zipData() { return !empty(base64zipData); }

    public LaunchConfig clearZipData() { setBase64zipData(null); return this; }

    public static final String ZIP_FILE_DIR = "configs";

    @Transient @JsonIgnore
    protected File getZipFileDir () { return LaunchApiConfiguration.configDir(ZIP_FILE_DIR); }

    @Transient @JsonIgnore
    public File getZipFile() {
        return new File(getZipFileDir(), "cloudos-launch-config-"+getUuid()+".zip.enc");
    }

    /**
     * Read a launch config zipfile
     * @return The launch config, now containing the base64-encoded contents of the zipfile (might be huge)
     * todo: use an input stream for the zipfile
     */
    public LaunchConfig readZipData() {
        final Crypto crypto = getLaunchAccount().getCrypto();
        try {
            @Cleanup final InputStream in = new FileInputStream(getZipFile());
            setBase64zipData(Base64.encodeBytes(crypto.decrypt(in)));

        } catch (Exception e) {
            die("readZipData: "+e, e);
        }
        return this;
    }

    /**
     * Write a launch config zipfile
     * @return The launch config
     * todo: use an input stream for the zipfile
     */
    public LaunchConfig writeZipData() {
        if (!hasBase64zipData()) die("writeZipData: no base64zipData provided");
        try {
            @Cleanup final InputStream in = new ByteArrayInputStream(Base64.decode(getBase64zipData()));
            @Cleanup final OutputStream out = new FileOutputStream(getZipFile());
            getLaunchAccount().getCrypto().encrypt(in, out);

        } catch (Exception e) {
            die("preCreate: error writing base64zipData to disk: " + e, e);
        }
        return this;
    }

}
