package cloudos.launcher.service;

import cloudos.cslib.compute.CsCloud;
import cloudos.cslib.compute.CsCloudConfig;
import cloudos.dao.CloudOsEventDAO;
import cloudos.databag.BaseDatabag;
import cloudos.deploy.CloudOsChefDeployer;
import cloudos.deploy.CloudOsLaunchTaskBase;
import cloudos.launcher.dao.CloudConfigDAO;
import cloudos.launcher.dao.LaunchConfigDAO;
import cloudos.launcher.model.CloudConfig;
import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.server.LaunchApiConfiguration;
import cloudos.model.CsGeoRegion;
import cloudos.model.CsPlatform;
import cloudos.model.instance.CloudOsAppBundle;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.util.io.DeleteOnExit;
import org.cobbzilla.wizard.dao.DAO;
import org.cobbzilla.wizard.task.ITask;

import java.io.File;
import java.util.List;

import static cloudos.launcher.ApiConstants.CLOUD_FACTORY;
import static cloudos.launcher.server.LaunchApiConfiguration.configDir;
import static org.cobbzilla.util.daemon.ZillaRuntime.die;
import static org.cobbzilla.util.io.FileUtil.*;
import static org.cobbzilla.util.json.JsonUtil.fromJsonOrDie;
import static org.cobbzilla.util.security.ShaUtil.sha256_hex;

@Slf4j
public class InstanceLaunchTask
        extends CloudOsLaunchTaskBase<LaunchAccount, Instance, LauncherTaskResult>
        implements ITask<LauncherTaskResult> {

    private CloudConfigDAO cloudConfigDAO;
    private LaunchConfigDAO launchConfigDAO;
    private LaunchApiConfiguration configuration;

    private File stagingDir;
    private File initFilesDir;

    @Getter(lazy=true) private final BaseDatabag baseDatabag = initBaseDatabag();
    private String hostname;
    private String domain;

    private BaseDatabag initBaseDatabag() {
        final File baseDatabagFile = new File(abs(initFilesDir) + "/data_bags/cloudos/base.json");
        if (!baseDatabagFile.exists()) die("buildCloud: base databag not found: "+abs(baseDatabagFile));
        return fromJsonOrDie(baseDatabagFile, BaseDatabag.class);
    }

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

        stagingDir = mkdirOrDie(createTempDirOrDie(configDir("deploy-staging"), cloudOs().getName()));
        DeleteOnExit.schedule(log, stagingDir);

        // decrypt and unroll the zipfile
        initFilesDir = mkdirOrDie(new File(stagingDir, "init_files"));
        launchConfigDAO.findByUuid(cloudOs().getLaunch())
                .setLaunchAccount(admin())
                .decryptZipData(initFilesDir);

        // set cloudos name based on databag
        final BaseDatabag baseDatabag = getBaseDatabag();
        hostname = baseDatabag.getHostname();
        domain = baseDatabag.getParent_domain();

        // prep databags
        // Is djbdns the DNS provider? If so, enable cloudos-dns and djbdns apps, init cloudos-dns account


        // build app list
        final List<String> allApps = cloudOs().getAllApps();

        // add all apps found in solo.json

        // add all required apps
        for (String requiredApp : CloudOsAppBundle.required.getApps()) {
            if (!allApps.contains(requiredApp)) allApps.add(requiredApp);
        }

        // prep staging dir
        final File chefMaster = configuration.getChefMaster();
        if (!CloudOsChefDeployer.prepChefStagingDir(stagingDir, chefMaster, allApps, configuration)) {
            die("preLaunch: CloudOsChefDeployer.prepChefStagingDir error");
        }

        return super.preLaunch();
    }

    @Override protected CsCloud buildCloud() {

        final CloudConfig cloud = cloudConfigDAO.findByUuid(cloudOs().getCloud());
        if (cloud == null) die("CloudConfig not found: " + cloudOs().getCloud());
        cloud.setLaunchAccount(cloudOs().getLaunchAccount()).decrypt();

        final CsGeoRegion region = cloudOs().getCsRegion();

        final String groupPrefix = hostname + "-" + sha256_hex(admin().getUuid() + "-" + cloudOs().getName()).substring(0, 5);
        final CsCloudConfig config = new CsCloudConfig();
        config.setType(cloud.getCloudType());
        config.setAccountId(cloud.getAccessKey());
        config.setAccountSecret(cloud.getSecretKey());
        config.setInstanceSize(cloudOs().getInstanceType());
        config.setRegion(region.getName());
        config.setImage(region.getImage(CsPlatform.ubuntu_14_lts));
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
