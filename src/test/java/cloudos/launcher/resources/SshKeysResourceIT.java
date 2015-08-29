package cloudos.launcher.resources;

import cloudos.launcher.model.SshKey;
import org.cobbzilla.util.http.HttpStatusCodes;
import org.cobbzilla.wizard.util.RestResponse;
import org.junit.Test;

import static cloudos.launcher.ApiConstants.KEYS_ENDPOINT;
import static org.cobbzilla.util.daemon.ZillaRuntime.empty;
import static org.cobbzilla.util.json.JsonUtil.fromJson;
import static org.cobbzilla.util.json.JsonUtil.toJson;
import static org.junit.Assert.*;

public class SshKeysResourceIT extends ApiResourceITBase {

    public static final String DOC_TARGET = "SSH Keys";

    @Test public void testCrud () throws Exception {

        SshKey[] keys;
        SshKey key;
        SshKey key2;
        SshKey found;

        apiDocs.startRecording(DOC_TARGET, "create, list, read, update and delete SSH keys");

        apiDocs.addNote("list keys, should be none");
        keys = fromJson(get(KEYS_ENDPOINT).json, SshKey[].class);
        assertTrue(empty(keys));

        key = randomSshKey();
        apiDocs.addNote("create a key");
        key = fromJson(post(KEYS_ENDPOINT + "/" + key.getName(), toJson(key)).json, SshKey.class);
        assertNotNull(key);

        key2 = randomSshKey();
        apiDocs.addNote("create another key");
        key2 = fromJson(post(KEYS_ENDPOINT + "/" + key2.getName(), toJson(key2)).json, SshKey.class);
        assertNotNull(key2);

        apiDocs.addNote("list keys, should be two");
        keys = fromJson(get(KEYS_ENDPOINT).json, SshKey[].class);
        assertEquals(2, keys.length);

        apiDocs.addNote("fetch a single configuration");
        found = fromJson(get(KEYS_ENDPOINT+"/"+key.getName()).json, SshKey.class);
        assertNotNull(found);
        assertEquals(key.getPublicKey(), found.getPublicKey());

        apiDocs.addNote("update a key - should fail, not allowed");
        final RestResponse response = doPost(KEYS_ENDPOINT + "/" + key.getName(), toJson(key));
        assertEquals(HttpStatusCodes.UNPROCESSABLE_ENTITY, response.status);

        apiDocs.addNote("delete a key");
        assertEquals(200, delete(KEYS_ENDPOINT + "/" + key.getName()).status);

        apiDocs.addNote("list keys, should be one");
        keys = fromJson(get(KEYS_ENDPOINT).json, SshKey[].class);
        assertEquals(1, keys.length);

        apiDocs.addNote("delete the other key");
        assertEquals(200, delete(KEYS_ENDPOINT + "/" + key2.getName()).status);

        apiDocs.addNote("list keys, should be none");
        keys = fromJson(get(KEYS_ENDPOINT).json, SshKey[].class);
        assertTrue(empty(keys));
    }

}
