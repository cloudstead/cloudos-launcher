package cloudos.launcher.auth;

import cloudos.launcher.dao.ApiTokenDAO;
import cloudos.launcher.model.LaunchAccount;
import org.cobbzilla.wizard.filters.auth.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaunchAuthProvider implements AuthProvider<LaunchAccount> {

    @Autowired private ApiTokenDAO tokenDAO;

    @Override
    public LaunchAccount find(String token) {

        final LaunchAccount account = tokenDAO.findAccount(token);
        if (account == null) return null;

        return account;
    }
}
