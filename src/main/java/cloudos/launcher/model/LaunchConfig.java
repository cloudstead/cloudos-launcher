package cloudos.launcher.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.wizard.model.UniquelyNamedEntity;

import javax.persistence.Entity;
import javax.persistence.Transient;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;

@Entity @Slf4j @Accessors(chain=true)
public class LaunchConfig extends UniquelyNamedEntity {

    @Transient
    @Getter @Setter private String base64zipData;
    public boolean hasBase64zipData() { return !empty(base64zipData); }

    public LaunchConfig clearZipData() { setBase64zipData(null); return this; }

}
