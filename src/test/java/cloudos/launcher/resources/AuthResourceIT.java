package cloudos.launcher.resources;

import cloudos.launcher.model.LaunchAccount;
import cloudos.model.auth.ApiToken;
import cloudos.model.auth.ChangePasswordRequest;
import org.cobbzilla.util.http.HttpStatusCodes;
import org.cobbzilla.wizard.util.RestResponse;
import org.junit.Test;

import static cloudos.launcher.ApiConstants.*;
import static org.cobbzilla.util.json.JsonUtil.fromJson;
import static org.cobbzilla.util.json.JsonUtil.toJson;
import static org.cobbzilla.wizardtest.RandomUtil.randomName;
import static org.junit.Assert.assertEquals;

public class AuthResourceIT extends ApiResourceITBase {

    public static final String DOC_TARGET = "Accounts";

    @Override protected boolean shouldCreateUser() { return false; }

    @Test public void testAccountCrud () throws Exception {

        RestResponse response;
        apiDocs.startRecording(DOC_TARGET, "account management");

        final String name = "account-" + randomName(8);
        final String password = "password-" + randomName(8);

        apiDocs.addNote("register an account, get session token");
        response = post(AUTH_ENDPOINT + "/" + name, password);
        final String token = fromJson(response.json, ApiToken.class).getToken();
        setToken(token);

        final String newPassword = "this-is-a-different-password-than-"+password;
        final ChangePasswordRequest request = new ChangePasswordRequest(password, newPassword);
        apiDocs.addNote("change password");
        assertStatusOK(post(ACCOUNTS_ENDPOINT + "/" + EP_CHANGE_PASSWORD, toJson(request)));

        apiDocs.addNote("try to start a new session with wrong password, should fail");
        setToken(null);
        response = doPost(AUTH_ENDPOINT + "/" + name, password);
        assertEquals(HttpStatusCodes.FORBIDDEN, response.status);

        apiDocs.addNote("try to start a new session with correct password, should succeed and return new token");
        assertStatusOK(post(AUTH_ENDPOINT + "/" + name, newPassword));

        // go back to using previous (valid) token
        setToken(token);

        final String newName = name + "-updated";
        final LaunchAccount update = (LaunchAccount) new LaunchAccount().setName(newName);
        apiDocs.addNote("change account name");
        assertStatusOK(post(ACCOUNTS_ENDPOINT, toJson(update)));

        apiDocs.addNote("fetch account, verify name changed");
        final LaunchAccount updated = fromJson(get(ACCOUNTS_ENDPOINT).json, LaunchAccount.class);
        assertEquals(newName.toLowerCase(), updated.getName().toLowerCase());

        apiDocs.addNote("delete account, session will also be invalidated");
        assertStatusOK(post(ACCOUNTS_ENDPOINT+"/"+EP_DELETE, newPassword));

        apiDocs.addNote("verify session is no longer valid");
        response = doGet(ACCOUNTS_ENDPOINT);
        assertEquals(HttpStatusCodes.FORBIDDEN, response.status);
    }

}
