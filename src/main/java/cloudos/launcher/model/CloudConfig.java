package cloudos.launcher.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.util.security.Crypto;
import org.cobbzilla.wizard.model.UniquelyNamedEntity;
import org.cobbzilla.wizard.validation.HasValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity @Slf4j @Accessors(chain=true)
public class CloudConfig extends UniquelyNamedEntity {

    @Column(nullable=false, length=UUID_MAXLEN)
    @Getter @Setter private String account;

    @Transient @JsonIgnore
    @Getter private LaunchAccount launchAccount;
    public CloudConfig setLaunchAccount(LaunchAccount launchAccount) {
        this.launchAccount = launchAccount;
        this.account = launchAccount == null ? null : launchAccount.getUuid();
        return this;
    }

    @NotNull @HasValue(message="err.cloud.access.empty")
    @Size(max=2048, message="err.cloud.access.tooLong")
    @Column(nullable=false, length=2048)
    @Getter @Setter private String accessKey;

    @NotNull @HasValue(message="err.cloud.secret.empty")
    @Size(max=2048, message="err.cloud.secret.tooLong")
    @Column(nullable=false, unique=true, length=2048)
    @Getter @Setter private String secretKey;

    @Size(max=4096, message="err.cloud.optionalJson.tooLong")
    @Column(length=4096)
    @Getter @Setter private String optionalJson;

    public CloudConfig encrypt() {
        final Crypto crypto = getLaunchAccount().getCrypto();
        setAccessKey(crypto.encrypt(getAccessKey()));
        setSecretKey(crypto.encrypt(getSecretKey()));
        setOptionalJson(crypto.encrypt(getOptionalJson()));
        return this;
    }

    public CloudConfig decrypt() {
        final Crypto crypto = getLaunchAccount().getCrypto();
        setAccessKey(crypto.decrypt(getAccessKey()));
        setSecretKey(crypto.decrypt(getSecretKey()));
        setOptionalJson(crypto.decrypt(getOptionalJson()));
        return this;
    }

}
