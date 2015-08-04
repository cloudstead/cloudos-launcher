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

    private static RestServerLifecycleListener listener = new LaunchApiServerListener();

    @Override protected String getListenAddress() { return NetworkUtil.getLocalhostIpv4(); }

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
        env.put("DB_DATA_DIR", abs(LaunchApiConfiguration.configDir("db")));

        final String assetsDir = System.getenv("ASSETS_DIR");
        if (!empty(assetsDir)) env.put("ASSETS_DIR", assetsDir);

        return env;
    }
}
