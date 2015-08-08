package cloudos.launcher.auth;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.server.LaunchApiConfiguration;
import com.sun.jersey.spi.container.ContainerRequest;
import edu.emory.mathcs.backport.java.util.Collections;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.cobbzilla.util.collection.StringPrefixTransformer;
import org.cobbzilla.wizard.filters.auth.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Provider @Service
public class LaunchAuthFilter extends AuthFilter<LaunchAccount> {

    @Override public String getAuthTokenHeader() { return ApiConstants.H_TOKEN; }
    @Getter private final Set<String> skipAuthPaths = Collections.emptySet();

    private static final Set<String> SKIP_AUTH_PREFIXES = new HashSet<>(Arrays.asList(new String[] {
            ApiConstants.AUTH_ENDPOINT
    }));

    @Autowired private LaunchApiConfiguration configuration;
    @Autowired @Getter private LaunchAuthProvider authProvider;

    @Getter(lazy=true) private final Set<String> skipAuthPrefixes = initSkipAuthPrefixes();
    public Set<String> initSkipAuthPrefixes() {
        final StringPrefixTransformer transformer = new StringPrefixTransformer(configuration.getHttp().getBaseUri());
        return new HashSet<>(CollectionUtils.collect(SKIP_AUTH_PREFIXES, transformer));
    }

    @Override protected boolean isPermitted(LaunchAccount principal, ContainerRequest request) {
        return true;
    }

}