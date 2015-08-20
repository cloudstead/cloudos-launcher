package cloudos.launcher.main;

import lombok.Getter;
import lombok.Setter;
import org.cobbzilla.wizard.api.CrudOperation;
import org.kohsuke.args4j.Option;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;

public abstract class LauncherCrudOptionsBase<E> extends LauncherMainOptionsBase {

    public static final String USAGE_OPERATION = "The operation to perform.";
    public static final String OPT_OPERATION = "-o";
    public static final String LONGOPT_OPERATION = "--operation";
    @Option(name=OPT_OPERATION, aliases=LONGOPT_OPERATION, usage=USAGE_OPERATION)
    @Getter @Setter private CrudOperation operation = CrudOperation.read;

    public static final String USAGE_NAME = "Name of the cloud config. Required for create/update.";
    public static final String OPT_NAME = "-n";
    public static final String LONGOPT_NAME = "--name";
    @Option(name=OPT_NAME, aliases=LONGOPT_NAME, usage=USAGE_NAME)
    @Getter @Setter private String name;
    public boolean hasName () { return !empty(name); }

    public boolean isValidForWrite() {
        if (!hasName()) required("NAME");
        return hasName();
    }

    public boolean isCustomAction() { return false; }

    public abstract String getEndpoint();

    public abstract E getRequestObject();
}
