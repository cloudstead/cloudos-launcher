package cloudos.launcher.resources;

import cloudos.launcher.model.CloudConfig;
import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchConfig;
import org.junit.Test;

import static cloudos.launcher.ApiConstants.*;
import static org.cobbzilla.util.daemon.ZillaRuntime.empty;
import static org.cobbzilla.util.json.JsonUtil.fromJson;
import static org.cobbzilla.util.json.JsonUtil.toJson;
import static org.cobbzilla.wizardtest.RandomUtil.randomName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class InstancesResourceIT extends ApiResourceITBase {

    public static final String DOC_TARGET = "Manage Cloudstead Instances";

    @Test public void testLaunchInstance () throws Exception {

        apiDocs.startRecording(DOC_TARGET, "launch a new cloudstead");

        LaunchConfig launchConfig;
        CloudConfig cloudConfig;
        Instance instance;
        Instance instance2;
        Instance[] instances;

        apiDocs.addNote("list all instances, should be none");
        instances = fromJson(get(INSTANCES_ENDPOINT).json, Instance[].class);
        assertTrue(empty(instances));

        launchConfig = (LaunchConfig) new LaunchConfig().setBase64zipData(DUMMY_ZIP_DATA).setName(randomName());
        apiDocs.addNote("create a launch config");
        launchConfig = fromJson(post(CONFIGS_ENDPOINT + "/" + launchConfig.getName(), toJson(launchConfig)).json, LaunchConfig.class);
        assertNotNull(launchConfig);

        cloudConfig = randomCloudConfig();
        apiDocs.addNote("create a cloud config");
        cloudConfig = fromJson(post(CLOUDS_ENDPOINT + "/" + cloudConfig.getName(), toJson(cloudConfig)).json, CloudConfig.class);
        assertNotNull(cloudConfig);

        instance = (Instance) new Instance()
                .setLaunch(launchConfig.getUuid())
                .setCloud(cloudConfig.getUuid())
                .setName(randomName());
        apiDocs.addNote("create an instance");
        post(INSTANCES_ENDPOINT + "/" + instance.getName(), toJson(instance));

        instance2 = (Instance) new Instance()
                .setLaunch(launchConfig.getUuid())
                .setCloud(cloudConfig.getUuid())
                .setName(randomName());
        apiDocs.addNote("create another instance");
        post(INSTANCES_ENDPOINT + "/" + instance2.getName(), toJson(instance2));

        apiDocs.addNote("list all instances, should be two");
        instances = fromJson(get(INSTANCES_ENDPOINT).json, Instance[].class);
        assertEquals(2, instances.length);


    }
}
