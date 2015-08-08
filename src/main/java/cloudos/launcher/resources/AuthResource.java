package cloudos.launcher.resources;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.dao.ApiTokenDAO;
import cloudos.launcher.dao.LaunchAccountDAO;
import cloudos.launcher.model.LaunchAccount;
import cloudos.model.auth.ApiToken;
import com.qmino.miredot.annotations.ReturnType;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.util.security.Crypto;
import org.cobbzilla.wizard.model.HashedPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;
import static org.cobbzilla.util.security.CryptoUtil.string_decrypt;
import static org.cobbzilla.util.security.CryptoUtil.string_encrypt;
import static org.cobbzilla.wizard.resources.ResourceUtil.*;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(ApiConstants.AUTH_ENDPOINT)
@Service @Slf4j
public class AuthResource {

    @Autowired private LaunchAccountDAO accountDAO;
    @Autowired private ApiTokenDAO tokenDAO;

    /**
     * Create a new account or login to an existing account. If the account does not exist,
     * it will be created with the password provided. If the account does exist and the password
     * matches, the account is logged in. Otherwise a 403 Forbidden is returned
     *
     * @param name Name of the account to create or login as
     * @param password Password for the account
     *
     * @return A session token that can be used on subsequent API calls
     */
    @POST
    @Path("/{name}")
    @ReturnType("java.lang.String")
    public Response loginOrRegister (@PathParam("name") String name, String password) {

        if (empty(name)) return invalid();
        if (empty(password)) return invalid();

        final ApiToken token;
        LaunchAccount account = accountDAO.findByName(name);

        if (account == null) {
            account = (LaunchAccount) new LaunchAccount()
                    .setHashedPassword(new HashedPassword(password))
                    .setDataKey(string_encrypt(UUID.randomUUID().toString(), password))
                    .setName(name);
            account = accountDAO.create(account);

        } else if (!account.getHashedPassword().isCorrectPassword(password)) {
            return forbidden();
        }

        token = tokenDAO.createSession(account);
        account.setCrypto(new Crypto(string_decrypt(account.getDataKey(), password)));

        return ok(token);
    }

}
