package cloudos.launcher.resources;

import cloudos.cslib.compute.CsCloud;
import cloudos.cslib.compute.meta.CsCloudType;
import cloudos.launcher.model.CloudConfig;
import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchConfig;
import cloudos.launcher.model.support.InstanceRequest;
import cloudos.launcher.service.LauncherTaskResult;
import org.cobbzilla.util.system.Sleep;
import org.cobbzilla.wizard.task.TaskId;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static cloudos.launcher.ApiConstants.*;
import static org.cobbzilla.util.daemon.ZillaRuntime.empty;
import static org.cobbzilla.util.json.JsonUtil.fromJson;
import static org.cobbzilla.util.json.JsonUtil.toJson;
import static org.cobbzilla.wizardtest.RandomUtil.randomName;
import static org.junit.Assert.*;

public class InstancesResourceIT extends ApiResourceITBase {

    public static final String DOC_TARGET = "Manage Cloudstead Instances";

    public static final long LAUNCH_TIMEOUT = TimeUnit.MINUTES.toMillis(10);

    @Test public void testLaunchInstance () throws Exception {

        apiDocs.startRecording(DOC_TARGET, "launch a new cloudstead");

        LaunchConfig launchConfig;
        CloudConfig cloudConfig;

        InstanceRequest instanceRequest;
        Instance instance;
        InstanceRequest instanceRequest2;
        Instance instance2;

        Instance[] instances;
        TaskId taskId;
        LauncherTaskResult result;

        apiDocs.addNote("list all cloud types, so we know what are the valid instance types and regions");
        assertStatusOK(get(CLOUD_TYPES_ENDPOINT));

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

        final CsCloudType<? extends CsCloud> cloudType = cloudConfig.getCloudType();

        instanceRequest = new InstanceRequest()
                .setLaunchConfig(launchConfig.getName())
                .setCloud(cloudConfig.getName())
                .setInstanceType(cloudType.getSmallestInstanceType().getName())
                .setRegion(cloudType.getRegions().get(0).getName())
                .setName(randomName());
        apiDocs.addNote("create an instance");
        final String instanceUri = INSTANCES_ENDPOINT + "/" + instanceRequest.getName();
        instance = fromJson(post(instanceUri, toJson(instanceRequest)).json, Instance.class);

        instanceRequest2 = new InstanceRequest()
                .setLaunchConfig(launchConfig.getName())
                .setCloud(cloudConfig.getName())
                .setInstanceType(cloudType.getSmallestInstanceType().getName())
                .setRegion(cloudType.getRegions().get(0).getName())
                .setName(randomName());
        apiDocs.addNote("create another instance");
        final String instanceUri2 = INSTANCES_ENDPOINT + "/" + instanceRequest2.getName();
        instance2 = fromJson(post(instanceUri2, toJson(instanceRequest2)).json, Instance.class);

        apiDocs.addNote("list all instances, should be two");
        instances = fromJson(get(INSTANCES_ENDPOINT).json, Instance[].class);
        assertEquals(2, instances.length);

        apiDocs.addNote("launch an instance, get taskId");
        taskId = fromJson(post(instanceUri + "/launch", null).json, TaskId.class);

        Sleep.sleep(500);
        apiDocs.addNote("check status of instance");
        result = fromJson(get(TASKS_ENDPOINT + "/" + taskId.getUuid()).json, LauncherTaskResult.class);
        long start = System.currentTimeMillis();
        while (!result.isComplete() && System.currentTimeMillis() - start < LAUNCH_TIMEOUT) {
            Sleep.sleep(2000);
            apiDocs.addNote("check status of instance");
            result = fromJson(get(TASKS_ENDPOINT + "/" + taskId.getUuid()).json, LauncherTaskResult.class);
        }

        apiDocs.addNote("destroy instance we just launched");
        assertStatusOK(delete(instanceUri));

        apiDocs.addNote("list all instances, should be one");
        instances = fromJson(get(INSTANCES_ENDPOINT).json, Instance[].class);
        assertEquals(1, instances.length);

        apiDocs.addNote("destroy instance that wasn't launched");
        assertStatusOK(delete(instanceUri2));

        apiDocs.addNote("list all instances, should be none");
        instances = fromJson(get(INSTANCES_ENDPOINT).json, Instance[].class);
        assertTrue(empty(instances));
    }
}
