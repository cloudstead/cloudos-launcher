package cloudos.launcher.main;

import cloudos.launcher.ApiConstants;
import cloudos.model.auth.ApiToken;
import org.cobbzilla.wizard.main.MainApiBase;
import org.cobbzilla.wizard.util.RestResponse;

import static org.cobbzilla.util.json.JsonUtil.fromJson;

public abstract class LauncherMainBase<OPT extends LauncherMainOptionsBase> extends MainApiBase<OPT>{

    @Override protected void setSecondFactor(Object loginRequest, String token) { /* noop */ }

    @Override protected String getSessionId(RestResponse response) throws Exception {
        return fromJson(response.json, ApiToken.class).getToken();
    }

    @Override protected String getLoginUri(String account) {
        return ApiConstants.AUTH_ENDPOINT + "/" + account;
    }

    @Override protected String getApiHeaderTokenName() { return ApiConstants.H_TOKEN; }

    @Override protected Object buildLoginRequest(OPT options) { return options.getPassword(); }

}
