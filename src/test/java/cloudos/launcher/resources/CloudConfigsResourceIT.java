package cloudos.launcher.resources;

import cloudos.launcher.model.CloudConfig;
import org.junit.Test;

import static cloudos.launcher.ApiConstants.CLOUDS_ENDPOINT;
import static org.cobbzilla.util.daemon.ZillaRuntime.empty;
import static org.cobbzilla.util.json.JsonUtil.fromJson;
import static org.cobbzilla.util.json.JsonUtil.toJson;
import static org.junit.Assert.*;

public class CloudConfigsResourceIT extends ApiResourceITBase {

    public static final String DOC_TARGET = "Cloud Configs";

    @Test public void testCrud () throws Exception {

        CloudConfig[] configs;
        CloudConfig config;
        CloudConfig config2;
        CloudConfig found;

        apiDocs.startRecording(DOC_TARGET, "create, list, read, update and delete cloud configs");

        apiDocs.addNote("list configs, should be none");
        configs = fromJson(get(CLOUDS_ENDPOINT).json, CloudConfig[].class);
        assertTrue(empty(configs));

        config = randomCloudConfig();
        final String accessKey = config.getAccessKey();
        apiDocs.addNote("create a config");
        config = fromJson(post(CLOUDS_ENDPOINT + "/" + config.getName(), toJson(config)).json, CloudConfig.class);
        assertNotNull(config);

        config2 = randomCloudConfig();
        apiDocs.addNote("create another config");
        config2 = fromJson(post(CLOUDS_ENDPOINT + "/" + config2.getName(), toJson(config2)).json, CloudConfig.class);
        assertNotNull(config2);

        apiDocs.addNote("list configs, should be two");
        configs = fromJson(get(CLOUDS_ENDPOINT).json, CloudConfig[].class);
        assertEquals(2, configs.length);

        apiDocs.addNote("fetch a single configuration");
        found = fromJson(get(CLOUDS_ENDPOINT+"/"+config.getName()).json, CloudConfig.class);
        assertNotNull(found);
        assertTrue(accessKey.startsWith(found.getAccessKey().substring(0, CloudConfig.ACCESS_KEY_SHOW_CHARS)));
        assertEquals("secret key should not be exposed!", CloudConfig.SECRET_KEY_MASK, found.getSecretKey());

        apiDocs.addNote("update a config");
        config = fromJson(post(CLOUDS_ENDPOINT+"/"+config.getName(), toJson(config)).json, CloudConfig.class);
        assertNotNull(config);

        apiDocs.addNote("delete a config");
        assertEquals(200, delete(CLOUDS_ENDPOINT + "/" + config.getName()).status);

        apiDocs.addNote("list configs, should be one");
        configs = fromJson(get(CLOUDS_ENDPOINT).json, CloudConfig[].class);
        assertEquals(1, configs.length);

        apiDocs.addNote("delete the other config");
        assertEquals(200, delete(CLOUDS_ENDPOINT + "/" + config2.getName()).status);

        apiDocs.addNote("list configs, should be none");
        configs = fromJson(get(CLOUDS_ENDPOINT).json, CloudConfig[].class);
        assertTrue(empty(configs));
    }

}
