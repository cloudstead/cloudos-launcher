package cloudos.launcher.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.util.security.Crypto;
import org.cobbzilla.wizard.filters.auth.TokenPrincipal;
import org.cobbzilla.wizard.model.HashedPassword;
import org.cobbzilla.wizard.model.UniquelyNamedEntity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity @Slf4j @Accessors(chain=true) @EqualsAndHashCode(of={"uuid"})
public class LaunchAccount extends UniquelyNamedEntity implements TokenPrincipal {

    // Set by LaunchAuthFilter
    @JsonIgnore @Transient
    @Getter private String apiToken;
    public void setApiToken(String apiToken) { this.apiToken = apiToken; }

    // Set and encrypted with password at registration time
    @JsonIgnore
    @Column(length=2048, unique=true, nullable=false)
    @Getter @Setter private String dataKey;

    // At session-start time, LaunchAccountsResource decrypts dataKey with password and creates this
    @JsonIgnore @Transient
    @Getter @Setter private Crypto crypto;

    @Getter @Setter @Embedded
    @JsonIgnore private HashedPassword hashedPassword;

}
