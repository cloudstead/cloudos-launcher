package cloudos.launcher.resources;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.dao.LaunchAccountDAO;
import cloudos.launcher.model.LaunchAccount;
import cloudos.model.auth.ChangePasswordRequest;
import com.qmino.miredot.annotations.ReturnType;
import com.sun.jersey.api.core.HttpContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.cobbzilla.util.security.CryptoUtil.string_encrypt;
import static org.cobbzilla.wizard.resources.ResourceUtil.*;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(ApiConstants.ACCOUNTS_ENDPOINT)
@Service @Slf4j
public class LaunchAccountsResource {

    @Autowired private LaunchAccountDAO accountDAO;

    /**
     * Change account password
     * @param request a ChangePasswordRequest containing the current password and the new password
     * @return upon success, a 200 HTTP status and an empty document. upon failure, a 422 invalid
     * @statuscode 200 The account password was successfully changed
     * @statuscode 422 The current password is incorrect
     */
    @POST
    @Path(ApiConstants.EP_CHANGE_PASSWORD)
    @ReturnType("java.lang.Void")
    public Response changePassword (@Context HttpContext context,
                                    ChangePasswordRequest request) {

        final LaunchAccount account = userPrincipal(context);
        if (account == null) return invalid();
        if (!account.getHashedPassword().isCorrectPassword(request.getOldPassword())) return invalid();

        final String newPassword = request.getNewPassword();
        account.getHashedPassword().setPassword(newPassword);

        // re-encrypt data key with new password
        account.setDataKey(string_encrypt(account.getCrypto().getSecretKey(), newPassword));
        accountDAO.update(account);

        return ok();
    }

}
