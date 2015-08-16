package cloudos.launcher.main;

import cloudos.launcher.ApiConstants;
import org.cobbzilla.wizard.client.ApiClientBase;

public class CloudTypeMain extends LauncherMainBase<CloudTypeOptions> {

    public static void main (String[] args) { main(CloudTypeMain.class, args); }

    @Override protected void run() throws Exception {
        final CloudTypeOptions options = getOptions();
        final ApiClientBase api = getApiClient();
        final String uri = ApiConstants.CLOUD_TYPES_ENDPOINT;
        if (options.hasVendor()) {
            out(api.get(uri+"/"+options.getVendor()));

        } else {
            out(api.get(uri));
        }
    }

}
