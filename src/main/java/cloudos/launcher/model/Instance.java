package cloudos.launcher.model;

import cloudos.model.instance.CloudOsBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.util.security.Crypto;
import org.cobbzilla.wizard.validation.HasValue;
import org.cobbzilla.wizard.validation.IsUnique;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity @Slf4j @Accessors(chain=true)
@IsUnique(unique="name", daoBean="instanceDAO", message="{err.name.notUnique}")
public class Instance extends CloudOsBase {

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
}
