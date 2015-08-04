package cloudos.launcher.server;

import lombok.Setter;
import org.cobbzilla.wizard.server.config.DatabaseConfiguration;
import org.cobbzilla.wizard.server.config.HasDatabaseConfiguration;
import org.cobbzilla.wizard.server.config.RestServerConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.File;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;
import static org.cobbzilla.util.io.FileUtil.mkHomeDir;

public class LaunchApiConfiguration extends RestServerConfiguration implements HasDatabaseConfiguration {

    public static final String CONFIG_PREFIX = ".cloudos-launcher";

    public static File configDir(String dir) {
        if (empty(dir)) dir = "/";
        if (!dir.startsWith("/")) dir = "/" + dir;
        return mkHomeDir(CONFIG_PREFIX + dir);
    }

    @Setter private DatabaseConfiguration database;
    @Bean public DatabaseConfiguration getDatabase() { return database; }

}
