package cloudos.launcher.model.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.cobbzilla.wizard.validation.HasValue;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;

@NoArgsConstructor @AllArgsConstructor @Accessors(chain=true)
public class InstanceRequest {

    public InstanceRequest (String name) { setName(name); }

    // Name has a lot of restrictions: must have a value; min 3/max 20 alphanumeric chars; cannot be reserved word
    @HasValue(message="err.instance.name.required")
    @Size(max=20, message="err.instance.name.length")
    @Pattern(regexp = "[A-Za-z0-9]{3,}", message = "err.instance.name.invalid")
    @Getter @Setter private String name;

    @HasValue(message="err.instance.cloudConfig.required")
    @Getter @Setter private String cloud;

    @HasValue(message="err.instance.region.required")
    @Getter @Setter private String region = null;

    @Getter @Setter private String sshKey = null;
    public boolean hasSshKey () { return !empty(sshKey); }

    @HasValue(message="err.instance.launchConfig.required")
    @Getter @Setter private String launchConfig;

    @HasValue(message="err.instance.type.required")
    @Getter @Setter private String instanceType;

    // optional fields if we are using an existing instance
    @Getter @Setter private String instanceId;
    @Getter @Setter private String user;
    @Getter @Setter private String privateKey;
    @Getter @Setter private String keyPassphrase;

    @Getter @Setter private String additionalApps;

}
