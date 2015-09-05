package cloudos.launcher.auth;

import cloudos.launcher.dao.ApiTokenDAO;
import cloudos.launcher.model.LaunchAccount;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.wizard.filters.auth.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class LaunchAuthProvider implements AuthProvider<LaunchAccount> {

    @Autowired private ApiTokenDAO tokenDAO;

    @Override public LaunchAccount find(String token) {

        final LaunchAccount account;
        try {
            account = tokenDAO.findAccount(token);

        } catch (Exception e) {
            log.warn("find: tokenDAO.findAccount error: "+e, e);
            return null;
        }

        return account;
    }
}
