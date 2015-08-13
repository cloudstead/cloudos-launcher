package cloudos.launcher.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.wizard.model.UniquelyNamedEntity;
import org.cobbzilla.wizard.validation.HasValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity @Slf4j @Accessors(chain=true)
public class Instance extends UniquelyNamedEntity {

    @Column(nullable=false, length=UUID_MAXLEN)
    @Getter @Setter private String account;

    @Transient @JsonIgnore
    @Getter private LaunchAccount launchAccount;
    public Instance setLaunchAccount(LaunchAccount launchAccount) {
        this.launchAccount = launchAccount;
        this.account = launchAccount == null ? null : launchAccount.getUuid();
        return this;
    }

    // the UUID of a LaunchConfig
    @NotNull @HasValue(message="err.instance.launch.empty")
    @Column(nullable=false, updatable=false, length=UUID_MAXLEN)
    @Getter @Setter private String launch;

    // the UUID of a CloudConfig
    @NotNull @HasValue(message="err.instance.cloud.empty")
    @Column(nullable=false, updatable=false, length=UUID_MAXLEN)
    @Getter @Setter private String cloud;

    // JSON representing the instance, returned from cloudos-lib
    // includes IP addr, etc.
    // Can be big, so it's not put in DB. InstanceDAO reads/writes from disk.
    @Transient
    @Getter @Setter private String instanceJson;

}
