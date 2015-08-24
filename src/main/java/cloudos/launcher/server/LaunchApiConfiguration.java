package cloudos.launcher.server;

import cloudos.deploy.AppBundleResolver;
import lombok.Setter;
import org.cobbzilla.util.string.StringUtil;
import org.cobbzilla.wizard.server.config.DatabaseConfiguration;
import org.cobbzilla.wizard.server.config.HasDatabaseConfiguration;
import org.cobbzilla.wizard.server.config.RestServerConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.cobbzilla.util.daemon.ZillaRuntime.die;
import static org.cobbzilla.util.daemon.ZillaRuntime.empty;
import static org.cobbzilla.util.io.FileUtil.mkHomeDir;
import static org.cobbzilla.util.io.StreamUtil.loadResourceAsFile;
import static org.cobbzilla.util.io.StreamUtil.loadResourceAsStringOrDie;

public class LaunchApiConfiguration
        extends RestServerConfiguration
        implements HasDatabaseConfiguration, AppBundleResolver {

    public static final String CONFIG_PREFIX = ".cloudos-launcher";

    public static final String CHEF_MASTER = "chef-master";
    public static final String APP_BUNDLES = "app-bundles";
    public static final String SERVER_TARBALLS = "server-tarballs";

    public static File configDir(String dir) {
        if (empty(dir)) dir = "/";
        if (!dir.startsWith("/")) dir = "/" + dir;
        return mkHomeDir(CONFIG_PREFIX + dir);
    }

    @Setter private DatabaseConfiguration database;
    @Bean public DatabaseConfiguration getDatabase() { return database; }

    @Override public File getAppBundle(String name) throws Exception {
        final File bundleDir = configDir(APP_BUNDLES);
        final String bundleFilename = name + "-bundle.tar.gz";
        final File bundleFile = new File(bundleDir, bundleFilename);
        if (!bundleFile.exists()) {
            loadResourceAsFile(APP_BUNDLES + "/" + bundleFilename, bundleFile);
        }
        return bundleFile;
    }

    public File getServerTarball(String name) {
        final File tarballDir = configDir(SERVER_TARBALLS);
        final File tarball = new File(tarballDir, name);
        if (!tarball.exists()) {
            try {
                loadResourceAsFile(SERVER_TARBALLS + "/" + name, tarball);
            } catch (IOException e) {
                die("getServerTarball: error loading "+name+": "+e, e);
            }
        }
        return tarball;
    }

    public File getChefMaster() {
        final File chefMaster = configDir(CHEF_MASTER);
        final List<String> chefFiles = StringUtil.split(loadResourceAsStringOrDie(CHEF_MASTER+"/manifest.txt"), "\n");
        for (String name : chefFiles) {
            final File f = new File(chefMaster, name);
            if (!f.exists()) {
                try {
                    loadResourceAsFile(CHEF_MASTER + "/" + name, f);
                } catch (IOException e) {
                    die("getChefMaster: error loading "+name+": "+e, e);
                }
            }
        }
        return chefMaster;
    }

}
