package cloudos.launcher.main;

import cloudos.launcher.model.SshKey;
import org.cobbzilla.wizard.api.CrudOperation;

public class SshKeyMain extends LauncherCrudMainBase<SshKeyOptions, SshKey> {

    public static void main (String[] args) { main(SshKeyMain.class, args); }

    @Override protected void handleCustomAction() throws Exception {
        if (getOptions().getOperation() == CrudOperation.update) {
            die("update operations are not allowed. delete the key and re-create it.");
        }
    }

}
