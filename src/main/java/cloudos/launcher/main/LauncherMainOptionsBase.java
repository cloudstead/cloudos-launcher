package cloudos.launcher.main;

import org.cobbzilla.wizard.main.MainApiOptionsBase;

public class LauncherMainOptionsBase extends MainApiOptionsBase {

    @Override protected String getDefaultApiBaseUri() { return "http://127.0.0.1:18080/api"; }

    @Override protected String getPasswordEnvVarName() { return "CL_PASS"; }

}
