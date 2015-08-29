package cloudos.launcher.model;

import cloudos.cslib.ssh.CsKeyPair;
import cloudos.model.instance.CloudOsBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.util.security.Crypto;
import org.cobbzilla.wizard.filters.Scrubbable;
import org.cobbzilla.wizard.filters.ScrubbableField;
import org.cobbzilla.wizard.validation.HasValue;
import org.cobbzilla.wizard.validation.IsUnique;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;

@Entity @Slf4j @Accessors(chain=true)
@IsUnique(unique="name", daoBean="instanceDAO", message="{err.name.notUnique}")
public class Instance extends CloudOsBase implements Scrubbable {

    public static final ScrubbableField[] SCRUBBABLE = {
            new ScrubbableField(Instance.class, "privateKey", String.class),
            new ScrubbableField(Instance.class, "keyPassphrase", String.class)
    };
    @Override public ScrubbableField[] fieldsToScrub() { return SCRUBBABLE; }

    @Transient @JsonIgnore
    @Getter private LaunchAccount launchAccount;

    public Instance setLaunchAccount(LaunchAccount launchAccount) {
        this.launchAccount = launchAccount;
        setAdminUuid(launchAccount == null ? null : launchAccount.getUuid());
        return this;
    }

    @Override public Crypto getCrypto() { return launchAccount.getCrypto(); }

    // the UUID of a LaunchConfig
    @HasValue(message="err.instance.launchConfig.empty")
    @Column(nullable=false, updatable=false, length=UUID_MAXLEN)
    @Getter @Setter private String launch;

    // the UUID of a CloudConfig
    @HasValue(message="err.instance.cloudConfig.empty")
    @Column(nullable=false, updatable=false, length=UUID_MAXLEN)
    @Getter @Setter private String cloud;

    // the UUID of an SshKey
    @Column(updatable=false, length=UUID_MAXLEN)
    @Getter @Setter private String sshKey;

    // If set, this is the cloud-vendor-specific name of the instance to use.
    // It will never be destroyed by the launcher
    @Column(updatable=false, length=200)
    @Getter @Setter private String instanceId;
    public boolean hasInstanceId () { return !empty(instanceId); }

    @Column(length=16000)
    private String privateKey;
    public String getPrivateKey () { return empty(privateKey) ? privateKey : getCrypto().decrypt(privateKey); }
    public Instance setPrivateKey(String key) {
        this.privateKey = empty(key) ? key : getCrypto().encrypt(key);
        return this;
    }

    @Column(length=200)
    private String keyPassphrase;
    public String getKeyPassphrase () { return empty(keyPassphrase) ? keyPassphrase : getCrypto().decrypt(keyPassphrase); }
    public Instance setKeyPassphrase (String passphrase) {
        this.keyPassphrase = empty(passphrase) ? passphrase : getCrypto().encrypt(passphrase);
        return this;
    }

    @Override public boolean canTerminate() { return !hasInstanceId(); }

    @Transient @JsonIgnore public CsKeyPair getKeyPair() {
        return new CsKeyPair().setPrivateKey(getPrivateKey()).setPassphrase(getKeyPassphrase());
    }

}
