package cloudos.launcher.model;

import cloudos.model.SshPublicKeyBase;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity @Slf4j @Accessors(chain=true)
public class SshKey extends SshPublicKeyBase {

    @Column(nullable=false, length=UUID_MAXLEN)
    @Getter @Setter private String account;

}
