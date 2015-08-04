package cloudos.launcher.resources;

import cloudos.launcher.model.LaunchConfig;
import org.junit.Test;

import static cloudos.launcher.resources.LaunchConfigsResource.CONFIGS_ENDPOINT;
import static org.cobbzilla.util.daemon.ZillaRuntime.empty;
import static org.cobbzilla.util.json.JsonUtil.fromJson;
import static org.cobbzilla.util.json.JsonUtil.toJson;
import static org.cobbzilla.wizardtest.RandomUtil.randomName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LaunchConfigsResourceIT extends ApiResourceITBase {

    public static final String DOC_TARGET = "Launch Config";

    // this is actually a real zip file, base64 encoded. it simply contains a small text file named test.txt
    public static final String DUMMY_ZIP_DATA
    = "UEsDBBQAAAAIAOIKBEextbl4GAAAABkAAAAIABwAdGVzdC50eHRVVAkAA2h2wFVodsBVdXgLAAEE9QEAAAQUAAAAK8nILFYAoqzS4hKFRIWSVCCVlpmTygUAUEsBAh4DFAAAAAgA4goER7G1uXgYAAAAGQAAAAgAGAAAAAAAAQAAAKSBAAAAAHRlc3QudHh0VVQFAANodsBVdXgLAAEE9QEAAAQUAAAAUEsFBgAAAAABAAEATgAAAFoAAAAAAA==";

    @Test
    public void testCrud () throws Exception {

        LaunchConfig[] configs;
        LaunchConfig config;
        LaunchConfig config2;

        apiDocs.startRecording(DOC_TARGET, "create, list, read, update and delete launch configs");

        apiDocs.addNote("fetch all configs, should be none");
        configs = fromJson(get(CONFIGS_ENDPOINT).json, LaunchConfig[].class);
        assertTrue(empty(configs));

        config = (LaunchConfig) new LaunchConfig().setBase64zipData(DUMMY_ZIP_DATA).setName(randomName());
        apiDocs.addNote("create a config");
        config = fromJson(post(CONFIGS_ENDPOINT+"/"+config.getName(), toJson(config)).json, LaunchConfig.class);
        assertNotNull(config);

        config2 = (LaunchConfig) new LaunchConfig().setBase64zipData(DUMMY_ZIP_DATA).setName(randomName());
        apiDocs.addNote("create another config");
        config2 = fromJson(post(CONFIGS_ENDPOINT+"/"+config2.getName(), toJson(config2)).json, LaunchConfig.class);
        assertNotNull(config2);

        apiDocs.addNote("list configs, should be two");
        configs = fromJson(get(CONFIGS_ENDPOINT).json, LaunchConfig[].class);
        assertEquals(2, configs.length);

        apiDocs.addNote("fetch a full configuration");
        config = fromJson(get(CONFIGS_ENDPOINT+"/"+config.getName()).json, LaunchConfig.class);
        assertNotNull(config);
        assertEquals(DUMMY_ZIP_DATA, config.getBase64zipData());

        apiDocs.addNote("update a config");
        config = fromJson(post(CONFIGS_ENDPOINT+"/"+config.getName(), toJson(config)).json, LaunchConfig.class);
        assertNotNull(config);

        apiDocs.addNote("delete a config");
        assertEquals(200, delete(CONFIGS_ENDPOINT + "/" + config.getName()).status);

        apiDocs.addNote("list configs, should be one");
        configs = fromJson(get(CONFIGS_ENDPOINT).json, LaunchConfig[].class);
        assertEquals(1, configs.length);

        apiDocs.addNote("delete the other config");
        assertEquals(200, delete(CONFIGS_ENDPOINT + "/" + config2.getName()).status);

        apiDocs.addNote("list configs, should be none");
        configs = fromJson(get(CONFIGS_ENDPOINT).json, LaunchConfig[].class);
        assertTrue(empty(configs));
    }

}
