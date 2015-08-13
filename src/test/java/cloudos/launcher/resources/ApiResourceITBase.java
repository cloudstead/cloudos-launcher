package cloudos.launcher.resources;

import cloudos.launcher.model.CloudConfig;
import cloudos.launcher.server.LaunchApi;
import cloudos.launcher.server.LaunchApiConfiguration;
import cloudos.model.auth.ApiToken;
import org.cobbzilla.wizard.server.config.factory.ConfigurationSource;
import org.cobbzilla.wizard.server.config.factory.StreamConfigurationSource;
import org.cobbzilla.wizardtest.resources.ApiDocsResourceIT;
import org.junit.Before;

import java.util.List;
import java.util.Map;

import static cloudos.launcher.ApiConstants.AUTH_ENDPOINT;
import static cloudos.launcher.ApiConstants.H_TOKEN;
import static org.cobbzilla.util.json.JsonUtil.fromJson;
import static org.cobbzilla.wizardtest.RandomUtil.randomName;

public abstract class ApiResourceITBase extends ApiDocsResourceIT<LaunchApiConfiguration, LaunchApi> {

    // this is actually a real zip file, base64 encoded. it simply contains a small text file named test.txt
    public static final String DUMMY_ZIP_DATA
    = "UEsDBBQAAAAIAOIKBEextbl4GAAAABkAAAAIABwAdGVzdC50eHRVVAkAA2h2wFVodsBVdXgLAAEE9QEAAAQUAAAAK8nILFYAoqzS4hKFRIWSVCCVlpmTygUAUEsBAh4DFAAAAAgA4goER7G1uXgYAAAAGQAAAAgAGAAAAAAAAQAAAKSBAAAAAHRlc3QudHh0VVQFAANodsBVdXgLAAEE9QEAAAQUAAAAUEsFBgAAAAABAAEATgAAAFoAAAAAAA==";

    protected static CloudConfig randomCloudConfig() {
        return (CloudConfig) new CloudConfig().setAccessKey(randomName()).setSecretKey(randomName()).setName(randomName());
    }

    @Override protected String getTokenHeader() { return H_TOKEN; }

    @Override protected List<ConfigurationSource> getConfigurations() {
        return StreamConfigurationSource.fromResources(getClass(), "launcher-config-test.yml");
    }

    @Override protected Class<? extends LaunchApi> getRestServerClass() { return LaunchApi.class; }

    @Override protected Map<String, String> getServerEnvironment() throws Exception {
        return LaunchApi.getServerEnvironment();
    }

    protected boolean shouldCreateUser () { return true; }

    @Before public void createUser() throws Exception {
        if (!shouldCreateUser()) return;

        final String name = "account-" + randomName(8);
        final String password = "password-" + randomName(8);

        setToken(fromJson(post(AUTH_ENDPOINT + "/" + name, password).json, ApiToken.class).getToken());
    }
}
