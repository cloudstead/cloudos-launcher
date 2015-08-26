package cloudos.launcher.dao;

import cloudos.databag.BaseDatabag;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.model.LaunchConfig;
import cloudos.model.SslCertificateBase;
import lombok.Cleanup;
import org.cobbzilla.util.io.FileUtil;
import org.cobbzilla.util.io.TempDir;
import org.cobbzilla.wizard.dao.UniquelyNamedEntityDAO;
import org.cobbzilla.wizard.validation.SimpleViolationException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.io.File;
import java.util.List;

import static org.cobbzilla.util.daemon.ZillaRuntime.die;
import static org.cobbzilla.util.io.FileUtil.abs;

@Repository
public class LaunchConfigDAO extends UniquelyNamedEntityDAO<LaunchConfig> {

    @Override public LaunchConfig findByName(String name) {
        final LaunchConfig found = super.findByName(name);
        return found == null ? null : found.readZipData();
    }

    @Override public Object preCreate(@Valid LaunchConfig config) {
        validate(config);
        config.writeZipData();
        return super.preCreate(config);
    }

    @Override public Object preUpdate(@Valid LaunchConfig config) {
        validate(config);
        config.writeZipData();
        return super.preUpdate(config);
    }

    private void validate(LaunchConfig config) {
        // ensure certificate is for correct hostname
        @Cleanup final TempDir tempDir = new TempDir();
        config.decryptZipData(tempDir);
        final BaseDatabag base = BaseDatabag.fromChefRepo(tempDir);
        final File certFile = new File(abs(tempDir) + "/certs/cloudos/"+base.getSsl_cert_name()+".pem");
        SslCertificateBase cert;
        try {
            cert = new SslCertificateBase().setPem(FileUtil.toStringOrDie(certFile));
        } catch (Exception e) {
            throw new SimpleViolationException("{err.cert.invalid}");
        }
        final String fqdn = base.getHostname() + "." + base.getParent_domain();
        if (!cert.isValidForHostname(fqdn)) throw new SimpleViolationException("{err.cert.wrongName}");
    }

    @Override public void delete(String uuid) {
        final LaunchConfig config = findByUuid(uuid);
        if (config == null) die("not found: "+uuid);
        final File zipFile = config.getZipFile();
        if (!zipFile.delete()) die("Error deleting zipFile: "+abs(zipFile));
        super.delete(uuid);
    }

    public List<LaunchConfig> findByAccount(LaunchAccount account) {
        final List<LaunchConfig> configs = findByField("account", account.getUuid());
        for (LaunchConfig c : configs) c.setLaunchAccount(account);
        return configs;
    }

    public LaunchConfig findByAccountAndName(LaunchAccount account, String name) {
        return findByAccountAndName(account, name, true);
    }

    public LaunchConfig findByAccountAndName(LaunchAccount account, String name, boolean loadZipData) {
        final LaunchConfig config = uniqueResult(criteria().add(
                Restrictions.and(
                        Restrictions.eq("account", account.getUuid()),
                        Restrictions.or(
                                Restrictions.eq("name", nameValue(name)),
                                Restrictions.eq("uuid", nameValue(name))))));
        if (config == null) return null;
        config.setLaunchAccount(account);
        return loadZipData ? config.readZipData() : config;
    }
}
