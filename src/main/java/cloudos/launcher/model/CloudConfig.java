package cloudos.launcher.model;

import cloudos.cslib.compute.CsCloud;
import cloudos.cslib.compute.meta.CsCloudType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.util.security.Crypto;
import org.cobbzilla.wizard.filters.CustomScrubbage;
import org.cobbzilla.wizard.filters.ScrubbableField;
import org.cobbzilla.wizard.model.UniquelyNamedEntity;
import org.cobbzilla.wizard.validation.HasValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static cloudos.launcher.ApiConstants.CLOUD_TYPE_FACTORY;

@Entity @Slf4j @Accessors(chain=true)
public class CloudConfig extends UniquelyNamedEntity implements CustomScrubbage {

    private static final ScrubbableField[] SCRUB = new ScrubbableField[]{
            new ScrubbableField(CloudConfig.class, "accessKey", String.class),
            new ScrubbableField(CloudConfig.class, "secretKey", String.class)
    };
    @Override public ScrubbableField[] fieldsToScrub() { return SCRUB; }

    @Override
    public void scrub(Object entity, ScrubbableField field) {
        switch (field.name) {
            case "accessKey":
                ((CloudConfig) entity).setAccessKey(accessKey.substring(0, 5) + "...");
                break;
            case "secretKey":
                ((CloudConfig) entity).setSecretKey("-secret-");
                break;
        }
    }

    @Column(nullable=false, length=UUID_MAXLEN)
    @Getter @Setter private String account;

    @Transient @JsonIgnore
    @Getter private LaunchAccount launchAccount;
    public CloudConfig setLaunchAccount(LaunchAccount launchAccount) {
        this.launchAccount = launchAccount;
        this.account = launchAccount == null ? null : launchAccount.getUuid();
        return this;
    }

    @NotNull @HasValue(message="err.cloud.vendor.required")
    @Size(max=100, message="err.cloud.vendor.length")
    @Column(nullable=false, length=100)
    @Getter @Setter private String vendor;

    @NotNull @HasValue(message="err.cloud.access.required")
    @Size(max=2048, message="err.cloud.access.length")
    @Column(nullable=false, length=2048)
    @Getter @Setter private String accessKey;

    @NotNull @HasValue(message="err.cloud.secret.required")
    @Size(max=2048, message="err.cloud.secret.length")
    @Column(nullable=false, unique=true, length=2048)
    @Getter @Setter private String secretKey;

    @Size(max=4096, message="err.cloud.optionalJson.length")
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

    @Transient @JsonIgnore
    public CsCloudType<? extends CsCloud> getCloudType() { return CLOUD_TYPE_FACTORY.fromType(vendor); }
}
