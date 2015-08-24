package cloudos.launcher.service;

import cloudos.cslib.compute.CsCloud;
import cloudos.cslib.compute.CsCloudConfig;
import cloudos.dao.CloudOsEventDAO;
import cloudos.databag.BaseDatabag;
import cloudos.databag.CloudOsDatabag;
import cloudos.deploy.CloudOsLaunchTaskBase;
import cloudos.launcher.dao.CloudConfigDAO;
import cloudos.launcher.dao.LaunchConfigDAO;
import cloudos.launcher.model.CloudConfig;
import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.server.LaunchApiConfiguration;
import cloudos.model.CsGeoRegion;
import cloudos.model.CsPlatform;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.cobbzilla.util.collection.SingletonList;
import org.cobbzilla.wizard.dao.DAO;
import org.cobbzilla.wizard.task.ITask;

import java.io.File;
import java.util.List;

import static cloudos.launcher.ApiConstants.CLOUD_FACTORY;
import static org.cobbzilla.util.daemon.ZillaRuntime.die;
import static org.cobbzilla.util.io.FileUtil.*;
import static org.cobbzilla.util.json.JsonUtil.toJsonOrDie;
import static org.cobbzilla.util.security.ShaUtil.sha256_hex;

@Slf4j
public class InstanceLaunchTask
        extends CloudOsLaunchTaskBase<LaunchAccount, Instance, LauncherTaskResult>
        implements ITask<LauncherTaskResult> {

    public static final String CLOUDOS_SERVER_TARBALL = "cloudos-server.tar.gz";
    private CloudConfigDAO cloudConfigDAO;
    private LaunchConfigDAO launchConfigDAO;
    private LaunchApiConfiguration configuration;

    protected BaseDatabag initBaseDatabag() {
        writeZipData();
        return super.initBaseDatabag();
    }

    private void writeZipData() {
        launchConfigDAO.findByUuid(cloudOs().getLaunch())
                .setLaunchAccount(admin())
                .decryptZipData(getInitFilesDir());
    }

    @Override protected File createInitFilesDir(String dir) { return LaunchApiConfiguration.configDir(dir); }
    @Override protected File createChefDir(String dir) { return LaunchApiConfiguration.configDir(dir); }

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

        // build app list
        final List<String> allApps = cloudOs().getAllApps();

        // prep staging dir
        final File chefMaster = configuration.getChefMaster();
        final File stagingDir = cloudOs().getStagingDirFile();
        if (!prepChefRepo(stagingDir, chefMaster, allApps, configuration)) {
            die("preLaunch: CloudOsChefDeployer.prepChefStagingDir error");
        }

        // set cloudos name based on databag
        final BaseDatabag baseDatabag = getBaseDatabag();
        final String hostname = baseDatabag.getHostname();
        final String domain = baseDatabag.getParent_domain();

        // prep databags
        final CloudOsDatabag cloudOsDatabag = getCloudOsDatabag();
        if (!cloudOsDatabag.hasServerTarball()) {
            final File destFile = new File(abs(stagingDir) + "/data_files/cloudos/"+CLOUDOS_SERVER_TARBALL);
            copyFile(configuration.getServerTarball(CLOUDOS_SERVER_TARBALL), destFile);
            cloudOsDatabag.setServer_tarball("@data_files/cloudos/" + CLOUDOS_SERVER_TARBALL);
        }
        toFileOrDie(getCloudOsDatabagFile(), toJsonOrDie(cloudOsDatabag));

        // Is djbdns the DNS provider? If so, enable cloudos-dns and djbdns apps, init cloudos-dns account

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

}
