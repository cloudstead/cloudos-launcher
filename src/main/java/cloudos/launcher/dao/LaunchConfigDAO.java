package cloudos.launcher.dao;

import cloudos.databag.BaseDatabag;
import cloudos.databag.CloudOsDatabag;
import cloudos.databag.DnsMode;
import cloudos.dns.DnsClient;
import cloudos.dns.service.DynDnsManager;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.model.LaunchConfig;
import cloudos.model.SslCertificateBase;
import cloudos.server.DnsConfiguration;
import lombok.Cleanup;
import org.apache.commons.io.FileUtils;
import org.cobbzilla.util.dns.DnsManager;
import org.cobbzilla.util.dns.DnsRecordMatch;
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
        return super.preCreate(config);
    }

    @Override public Object preUpdate(@Valid LaunchConfig config) {
        validate(config);
        return super.preUpdate(config);
    }

    protected void validate(LaunchConfig config) {
        // ensure certificate is for correct hostname
        boolean ok = false;
        try {
            config.writeZipData();

            @Cleanup final TempDir tempDir = new TempDir();
            config.decryptZipData(tempDir);
            final BaseDatabag baseDatabag = BaseDatabag.fromChefRepo(tempDir);
            final CloudOsDatabag cloudOsDatabag = CloudOsDatabag.fromChefRepo(tempDir);

            validateCertificate(config, tempDir, baseDatabag);
            validateDns(config, tempDir, baseDatabag, cloudOsDatabag);
            ok = true;

        } catch (SimpleViolationException e) {
            throw e;

        } catch (Exception e) {
            throw new SimpleViolationException("{err.config.error}", e.toString());

        } finally {
            final File zipFile = config.getZipFile();
            if (!ok && zipFile.exists()) FileUtils.deleteQuietly(zipFile);
        }
    }

    protected void validateCertificate(LaunchConfig config, TempDir tempDir, BaseDatabag baseDatabag) {
        boolean ok = false;
        try {
            final File certFile = new File(abs(tempDir) + "/certs/cloudos/" + baseDatabag.getSsl_cert_name() + ".pem");
            SslCertificateBase cert;
            try {
                cert = new SslCertificateBase().setPem(FileUtil.toStringOrDie(certFile));
            } catch (Exception e) {
                throw new SimpleViolationException("{err.cert.invalid}");
            }
            final String fqdn = baseDatabag.getHostname() + "." + baseDatabag.getParent_domain();
            if (!cert.isValidForHostname(fqdn)) throw new SimpleViolationException("{err.cert.wrongName}");
            ok = true;

        } catch (SimpleViolationException e) {
            throw e;

        } catch (Exception e) {
            throw new SimpleViolationException("{err.cert.invalid}");

        } finally {
            final File zipFile = config.getZipFile();
            if (!ok && zipFile.exists()) FileUtils.deleteQuietly(zipFile);
        }
    }

    protected void validateDns(LaunchConfig config, TempDir tempDir, BaseDatabag baseDatabag, CloudOsDatabag cloudOsDatabag) {

        final DnsConfiguration cloudOsDatabagDns = cloudOsDatabag.getDns();
        final DnsMode dnsMode = cloudOsDatabagDns.getMode();
        DnsManager dnsManager;
        switch (dnsMode) {
            case dyn:
                // cloudos will talk to Dyn directly. Test the connection...
                dnsManager = new DynDnsManager(cloudOsDatabagDns);
                try {
                    dnsManager.list(new DnsRecordMatch());
                } catch (Exception e) {
                    throw new SimpleViolationException("err.dns.dyn.error", "an error occurred trying to talk to Dyn", e.toString());
                }
                break;

            case cdns:
                // cloudos will talk to another cloudos-dns server (which might then talk to Dyn, or manage a local DNS server)
                dnsManager = new DnsClient(cloudOsDatabagDns);
                try {
                    dnsManager.list(new DnsRecordMatch());
                } catch (Exception e) {
                    throw new SimpleViolationException("err.dns.cdns.error", "an error occurred trying to talk to cloudos-dns", e.toString());
                }
                // todo: build a cloudos-dns bundle for the user so they can download/install it on their external DNS server
                break;

            case internal:
                // cloudos will talk to its own local cloudos-dns server, which will manage a local djbdns server
                // nothing to test from here
                break;

            default:
                die("setupDns: Unsupported mode: "+ dnsMode);
                break;
        }
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
