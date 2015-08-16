package cloudos.launcher.main;

import org.cobbzilla.wizard.client.ApiClientBase;

import static org.cobbzilla.util.json.JsonUtil.toJson;

public class LauncherCrudMainBase<OPT extends LauncherCrudOptionsBase<R>, R> extends LauncherMainBase<OPT> {

    protected void handleCustomAction() throws Exception {
        die("handleCustomAction was not overridden in "+getClass().getName());
    }

    @Override protected void run() throws Exception {
        final OPT options = getOptions();
        final ApiClientBase api = getApiClient();
        String uri = options.getEndpoint();

        if (options.isCustomAction()) {
            handleCustomAction();

        } else {
            switch (options.getOperation()) {
                case read:
                    if (options.hasName()) uri += "/" + options.getName();
                    out(api.get(uri));
                    break;

                case create:
                case update:
                    if (!options.isValidForWrite()) die("Missing one or more required arguments");
                    uri += "/" + options.getName();
                    R requestObject = options.getRequestObject();
                    out(api.post(uri, toJson(requestObject)));
                    break;

                case delete:
                    if (!options.hasName()) die("Name is required");
                    uri += "/" + options.getName();
                    out(api.delete(uri));
                    break;

                default:
                    die("Invalid operation: " + options.getOperation());
            }
        }
    }
}
