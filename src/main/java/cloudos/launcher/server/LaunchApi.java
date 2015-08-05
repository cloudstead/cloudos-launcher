package cloudos.launcher.server;

import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.util.system.NetworkUtil;
import org.cobbzilla.wizard.server.RestServerBase;
import org.cobbzilla.wizard.server.RestServerLifecycleListener;
import org.cobbzilla.wizard.server.config.factory.ConfigurationSource;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;
import static org.cobbzilla.util.io.FileUtil.*;

@Slf4j
public class LaunchApi extends RestServerBase<LaunchApiConfiguration> {

    public static final String[] API_CONFIG_YML = {"launcher-config.yml"};

    public static final String ENV_DB_DATA_DIR = "DB_DATA_DIR";
    public static final String ENV_ASSETS = "LAUNCHER_ASSETS_DIR";
    public static final String ENV_LISTEN_ADDR = "LAUNCHER_LISTEN_ADDR";
    public static final String ENV_PORT = "LAUNCHER_PORT";

    private static RestServerLifecycleListener listener = new LaunchApiServerListener();

    @Override protected String getListenAddress() {
        final String addr = System.getenv(ENV_LISTEN_ADDR);
        return empty(addr) ? NetworkUtil.getLocalhostIpv4() : addr;
    }

    // args are ignored, config is loaded from the classpath, and env is hardcoded
    public static void main(String[] args) throws Exception {

        // redirect JUL -> logback using slf4j
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        // launch the server. the listener will start a web browser once the server is up
        main(LaunchApi.class, listener, getConfigurationSources(), getServerEnvironment());
    }

    public static List<ConfigurationSource> getConfigurationSources() {
        // load config yml from classpath
        return getStreamConfigurationSources(LaunchApi.class, API_CONFIG_YML);
    }

    public static Map<String, String> getServerEnvironment() {

        // fixed environment, ignore System.getenv except for ASSETS_DIR
        final Map<String, String> env = new HashMap<>();
        env.put("HOME", getUserHomeDir());
        env.put(ENV_DB_DATA_DIR, abs(LaunchApiConfiguration.configDir("db")));

        final String assetsDir = System.getenv(ENV_ASSETS);
        if (!empty(assetsDir)) env.put(ENV_ASSETS, assetsDir);

        final String port = System.getenv(ENV_PORT);
        env.put(ENV_PORT, empty(port) ? "0" : port);

        return env;
    }
}
