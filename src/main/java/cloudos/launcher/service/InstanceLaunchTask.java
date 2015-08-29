package cloudos.launcher.service;

import cloudos.cslib.compute.CsCloud;
import cloudos.cslib.compute.CsCloudConfig;
import cloudos.cslib.compute.instance.CsInstance;
import cloudos.cslib.compute.instance.CsInstanceRequest;
import cloudos.dao.CloudOsEventDAO;
import cloudos.databag.*;
import cloudos.deploy.CloudOsLaunchTaskBase;
import cloudos.dns.DnsClient;
import cloudos.dns.databag.CloudOsDnsDatabag;
import cloudos.dns.databag.DjbdnsDatabag;
import cloudos.dns.service.DynDnsManager;
import cloudos.launcher.dao.CloudConfigDAO;
import cloudos.launcher.dao.LaunchConfigDAO;
import cloudos.launcher.model.CloudConfig;
import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.model.LaunchConfig;
import cloudos.launcher.server.LaunchApiConfiguration;
import cloudos.model.CsGeoRegion;
import cloudos.model.CsPlatform;
import cloudos.server.DnsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.cobbzilla.util.collection.SingletonList;
import org.cobbzilla.util.dns.DnsManager;
import org.cobbzilla.util.dns.DnsRecordMatch;
import org.cobbzilla.util.dns.DnsServerType;
import org.cobbzilla.util.security.bcrypt.BCryptUtil;
import org.cobbzilla.wizard.dao.DAO;
import org.cobbzilla.wizard.task.ITask;
import rooty.toots.chef.ChefSolo;

import java.io.File;
import java.util.List;

import static cloudos.launcher.ApiConstants.CLOUD_FACTORY;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.cobbzilla.util.daemon.ZillaRuntime.die;
import static org.cobbzilla.util.daemon.ZillaRuntime.empty;
import static org.cobbzilla.util.io.FileUtil.abs;
import static org.cobbzilla.util.io.FileUtil.copyFile;
import static org.cobbzilla.util.security.ShaUtil.sha256_hex;

@Slf4j
public class InstanceLaunchTask
        extends CloudOsLaunchTaskBase<LaunchAccount, Instance, LauncherTaskResult>
        implements ITask<LauncherTaskResult> {

    public static final String CLOUDOS_SERVER_TARBALL = "cloudos-server.tar.gz";
    private CloudConfigDAO cloudConfigDAO;
    private LaunchConfigDAO launchConfigDAO;
    private LaunchApiConfiguration configuration;

    private void writeZipData(File dir) {
        final LaunchConfig config = launchConfigDAO.findByUuid(cloudOs().getLaunch()).setLaunchAccount(admin());
        config.decryptZipData(dir);
    }

    @Override protected File createInitFilesDir(String dir) {
        final File chefRepo = LaunchApiConfiguration.configDir(dir);
        writeZipData(chefRepo);
        return chefRepo;
    }
    @Override protected File createChefDir(String dir) { return LaunchApiConfiguration.configDir(dir); }

    @Override public String getJsonEdit() { return configuration.getJsonEdit(); }

    @Override public List<File> getCookbookSources() { return new SingletonList<>(new File("/nopath-"+System.currentTimeMillis()+ RandomStringUtils.randomAlphanumeric(10))); }

    @Override public Mode getMode() { return Mode.inline; }

    public InstanceLaunchTask(LaunchAccount account,
                              Instance instance,
                              DAO<Instance> cloudOsDAO,
                              CloudConfigDAO cloudConfigDAO,
                              LaunchConfigDAO launchConfigDAO,
                              LaunchApiConfiguration configuration,
                              CloudOsEventDAO eventDAO) {
        init(account, instance, cloudOsDAO, eventDAO);
        this.cloudConfigDAO = cloudConfigDAO;
        this.launchConfigDAO = launchConfigDAO;
        this.configuration = configuration;
    }

    @Override protected String getSimpleHostname() { return getBaseDatabag().getHostname(); }

    @Override protected boolean preLaunch() {

        final File chefMaster = configuration.getChefMaster();
        final File stagingDir = getStagingDir();

        // build app list
        final List<String> allApps = cloudOs().getAllApps();

        // prep databags
        final CloudOsDatabag cloudOsDatabag = getCloudOsDatabag();
        if (!cloudOsDatabag.hasServerTarball()) {
            final File destFile = new File(abs(stagingDir) + "/data_files/cloudos/"+CLOUDOS_SERVER_TARBALL);
            copyFile(configuration.getServerTarball(CLOUDOS_SERVER_TARBALL), destFile);
            // note that after substitution at chef-runtime, @data_files becomes chef-repo/data_files/{appname}
            cloudOsDatabag.setServer_tarball("@data_files/" + CLOUDOS_SERVER_TARBALL);
        }

        // prep dns
        if (!configureDns(stagingDir, cloudOsDatabag)) return false;

        // write cloudos init databag to pickup any changes above
        cloudOsDatabag.toChefRepo(stagingDir);

        // prep staging dir
        if (!prepChefRepo(stagingDir, chefMaster, allApps, configuration)) {
            error("err.instance.launch.prepStaging", "an error occurred preparing the chef staging directory");
            return false;
        }

        return super.preLaunch();
    }

    @Override protected CsCloud buildCloud() {

        final CloudConfig cloud = cloudConfigDAO.findByUuid(cloudOs().getCloud());
        if (cloud == null) die("CloudConfig not found: " + cloudOs().getCloud());
        cloud.setLaunchAccount(cloudOs().getLaunchAccount()).decrypt();

        final CsGeoRegion region = cloudOs().getCsRegion();

        final BaseDatabag baseDatabag = getBaseDatabag();
        final String hostname = baseDatabag.getHostname();
        final String domain = baseDatabag.getParent_domain();

        final String groupPrefix = hostname + "-" + sha256_hex(admin().getUuid() + "-" + cloudOs().getName()).substring(0, 5);
        final CsCloudConfig config = new CsCloudConfig();
        final String instanceType = cloudOs().getInstanceType();

        config.setType(cloud.getCloudType());
        config.setAccountId(cloud.getAccessKey());
        config.setAccountSecret(cloud.getSecretKey());
        config.setInstanceSize(instanceType);
        config.setRegion(region.getName());
        config.setImage(region.getImage(instanceType, CsPlatform.ubuntu_14_lts));
        config.setGroupPrefix(groupPrefix);
        config.setUser(hostname);
        config.setDomain(domain);

        try {
            return CLOUD_FACTORY.buildCloud(config);
        } catch (Exception e) {
            return die("buildCloud: "+e, e);
        }
    }

    @Override protected boolean addAppStoreAccount(String hostname, String ucid) {
        log.info("addAppStoreAccount called");
        return true;
    }

    @Override protected CsInstance createInstance(CsInstanceRequest instanceRequest) throws Exception {
        if (cloudOs().hasInstanceId()) {
            return getCloud().findInstance(cloudOs().getInstanceId(), cloudOs().getName(), cloudOs().getKeyPair());
        } else {
            return super.createInstance(instanceRequest);
        }
    }

    @Override protected boolean setupDns() {
        // this is all taken care of in configureDns, called by preLaunch
        return super.setupDns();
    }

    private boolean configureDns(File stagingDir, CloudOsDatabag cloudOsDatabag) {

        // what kind of DNS are we going to use?
        final DnsConfiguration cloudOsDatabagDns = cloudOsDatabag.getDns();
        final DnsMode dnsMode = cloudOsDatabagDns.getMode();
        DnsManager dnsManager;
        switch (dnsMode) {
            case dyn:
                // cloudos will talk to Dyn directly. check connection
                dnsManager = new DynDnsManager(cloudOsDatabagDns);
                try {
                    dnsManager.list(new DnsRecordMatch());
                } catch (Exception e) {
                    error("err.dns.dyn.error", e.toString());
                    return false;
                }
                break;

            case cdns:
                // cloudos will talk to another cloudos-dns server (which might then talk to Dyn, or manage a local DNS server)
                // check the connection
                dnsManager = new DnsClient(cloudOsDatabagDns);
                try {
                    dnsManager.list(new DnsRecordMatch());
                } catch (Exception e) {
                    error("err.dns.cdns.error", e.toString());
                    return false;
                }
                break;

            case internal:
                return setupInternalDns(stagingDir, cloudOsDatabagDns);

            default:
                error("err.dns.mode.invalid", "setupDns: Unsupported mode: "+ dnsMode);
                return false;
        }

        return true;
    }

    private boolean setupInternalDns(File stagingDir, DnsConfiguration cloudOsDatabagDns) {

        // cloudos will talk to its own local cloudos-dns server, which will manage a local djbdns server
        // steps:
        //   - ensure djbdns databag is present
        //   - setup cloudos-dns databags (ports.json and init.json)
        //   - setup 'dns' block cloudos databags
        //   - ensure cloudos-dns and djbdns apps are in solo.json run list

        // ensure djbdns databag exists
        if (empty(DjbdnsDatabag.getChefFile(stagingDir))) {
            error("err.dns.djbdns.notFound", "djbdns mode was selected but no databag was found");
            return false;
        }

        final CloudOsDnsDatabag cdnsInitDatabag = CloudOsDnsDatabag.fromChefRepoOrNew(stagingDir);
        final PortsDatabag cdnsPortsDatabag = PortsDatabag.fromChefRepoOrNew(stagingDir, "cloudos-dns");

        // set cdns port and write databag
        cdnsPortsDatabag.setPrimary(4002);
        cdnsPortsDatabag.toChefRepo(stagingDir, "cloudos-dns");

        // the cdns admin is defined in cloudos-dns and referenced in cloudos
        final String cdnsAdminUser = "admin";
        final String cdnsAdminPassword = randomAlphanumeric(20);

        // this is the DNS config block for cloudos-dns -- not much here since we're using djbdns
        final DnsConfiguration dns = new DnsConfiguration();
        dns.setMode(DnsMode.internal);
        dns.setEnabled(true);

        // define cloudos-dns init databag, use same admin account
        cdnsInitDatabag
                .setAdmin(new NameAndPassword().setName(cdnsAdminUser).setPassword(BCryptUtil.hash(cdnsAdminPassword)))
                .setServer_type(DnsServerType.djbdns)
                .setDns(dns);
        cdnsInitDatabag.toChefRepo(stagingDir);

        // update "dns" block of cloudos init databag, point to internal
        // cloudos init databag will be written in preLaunch after this returns
        cloudOsDatabagDns.setMode(DnsMode.internal);
        cloudOsDatabagDns.setUser(cdnsAdminUser);
        cloudOsDatabagDns.setBaseUri("http://127.0.0.1:" + cdnsPortsDatabag.getPrimary() + "/api");
        cloudOsDatabagDns.setPassword(cdnsAdminPassword);

        try {
            final ChefSolo chefSolo = ChefSolo.fromChefRepo(stagingDir);
            chefSolo.insertApp("djbdns", stagingDir);
            chefSolo.insertApp("cloudos-dns", stagingDir);
            chefSolo.write(stagingDir);

        } catch (Exception e) {
            error("err.dns.updatingChef", "error updating solo.json to add djbdns and cloudos-dns: "+e);
            return false;
        }

        return true;
    }
}
