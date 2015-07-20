package cloudos.launcher.server;

import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.util.system.NetworkUtil;
import org.cobbzilla.wizard.server.RestServerBase;
import org.cobbzilla.wizard.server.RestServerLifecycleListener;
import org.cobbzilla.wizard.server.config.factory.ConfigurationSource;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.List;

@Slf4j
public class LaunchApi extends RestServerBase<LaunchApiConfiguration> {

    public static final String[] API_CONFIG_YML = {"launcher-config.yml"};

    private static RestServerLifecycleListener listener = new LaunchApiServerListener();

    @Override protected String getListenAddress() { return NetworkUtil.getLocalhostIpv4(); }

    // args are ignored, config is loaded from the classpath
    public static void main(String[] args) throws Exception {

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        final List<ConfigurationSource> configSources = getConfigurationSources();
        main(LaunchApi.class, listener, configSources);
    }

    public static List<ConfigurationSource> getConfigurationSources() {
        return getStreamConfigurationSources(LaunchApi.class, API_CONFIG_YML);
    }
}
