package cloudos.launcher.resources;

import cloudos.launcher.server.LaunchApi;
import cloudos.launcher.server.LaunchApiConfiguration;
import org.cobbzilla.wizard.server.config.factory.ConfigurationSource;
import org.cobbzilla.wizard.server.config.factory.StreamConfigurationSource;
import org.cobbzilla.wizardtest.resources.ApiDocsResourceIT;

import java.util.List;
import java.util.Map;

public class ApiResourceITBase extends ApiDocsResourceIT<LaunchApiConfiguration, LaunchApi> {

    @Override protected List<ConfigurationSource> getConfigurations() {
        return StreamConfigurationSource.fromResources(getClass(), "launcher-config-test.yml");
    }

    @Override protected Class<? extends LaunchApi> getRestServerClass() { return LaunchApi.class; }

    @Override protected Map<String, String> getServerEnvironment() throws Exception {
        return LaunchApi.getServerEnvironment();
    }

}
