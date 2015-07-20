package cloudos.launcher.server;

import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.util.http.URIUtil;
import org.cobbzilla.wizard.server.RestServer;
import org.cobbzilla.wizard.server.RestServerLifecycleListenerBase;
import org.cobbzilla.wizard.server.config.RestServerConfiguration;

import java.awt.*;

import static org.cobbzilla.util.daemon.ZillaRuntime.die;

@Slf4j
public class LaunchApiServerListener extends RestServerLifecycleListenerBase<LaunchApiConfiguration> {

    private static RestServer SERVER = null;
    public static RestServer getServer () { return SERVER; }

    @Override public void onStart(final RestServer server) {

        if (SERVER != null) die("onStart: server already running: "+SERVER.getConfiguration().getPublicUriBase());

        final LaunchApi apiServer = (LaunchApi) server;

        final RestServerConfiguration config = server.getConfiguration();
        config.setPublicUriBase("http://" + apiServer.getListenAddress() + ":" +config.getHttp().getPort());

        SERVER = server;
        final String baseUri = config.getPublicUriBase();
        final Thread appThread = new Thread(new Runnable() {
            @Override public void run() {
                final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(URIUtil.toUri(baseUri));
                    } catch (Exception e) {
                        final String msg = "onStart: error launching default browser with url '" + baseUri + "':  " + e;
                        log.error(msg, e);
                        die(msg, e);
                    }
                }
            }
        });
        appThread.setDaemon(true);
        appThread.start();
    }

}
