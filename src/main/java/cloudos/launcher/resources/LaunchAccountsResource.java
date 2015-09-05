package cloudos.launcher.resources;

import cloudos.launcher.ApiConstants;
import cloudos.launcher.dao.*;
import cloudos.launcher.model.LaunchAccount;
import cloudos.model.auth.ChangePasswordRequest;
import com.qmino.miredot.annotations.ReturnType;
import com.sun.jersey.api.core.HttpContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
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
    @Autowired private CloudConfigDAO cloudConfigDAO;
    @Autowired private LaunchConfigDAO launchConfigDAO;
    @Autowired private SshKeyDAO keyDAO;
    @Autowired private InstanceDAO instanceDAO;
    @Autowired private ApiTokenDAO tokenDAO;

    /**
     * Get current account details.
     * @param context current user session is retrieved from here
     * @return the LaunchAccount for the current session
     */
    @GET
    @ReturnType("cloudos.launcher.model.LaunchAccount")
    public Response getAccount (@Context HttpContext context) {
        final LaunchAccount account = userPrincipal(context);
        return account == null ? invalid() : ok(account);
    }

    /**
     * Edit account. Currently only the 'name' field can be edited.
     * @param context current user session is retrieved from here
     * @param request a LaunchAccount containing the updated data (currently only name can be updated)
     * @return upon success, a 200 HTTP status and an empty document. upon failure, a 422 invalid
     * @statuscode 200 The account was successfully updated
     */
    @POST
    @ReturnType("cloudos.launcher.model.LaunchAccount")
    public Response editAccount (@Context HttpContext context,
                                 LaunchAccount request) {
        final LaunchAccount account = userPrincipal(context);
        if (account == null) return invalid();
        account.setName(request.getName());
        return ok(accountDAO.update(account));
    }

    /**
     * Change account password
     * @param context current user session is retrieved from here
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

    /**
     * Delete an account and all data associated with it.
     * Deletes all cloud configs, launch configs including encrypted zip files, instance data and ssh keys.
     * @param context current user session is retrieved from here
     * @param password the current account password
     * @return upon success, a 200 HTTP status and an empty document. The session token used in
     */
    @POST
    @Path(ApiConstants.EP_DELETE)
    @ReturnType("java.lang.Void")
    public Response delete (@Context HttpContext context,
                            String password) {

        final LaunchAccount account = userPrincipal(context);
        if (account == null) return invalid();
        if (!account.getHashedPassword().isCorrectPassword(password)) return invalid();

        launchConfigDAO.deleteByAccount(account);
        cloudConfigDAO.deleteByAccount(account);
        instanceDAO.deleteByAccount(account);
        keyDAO.deleteByAccount(account);
        tokenDAO.invalidateAll(account);
        accountDAO.delete(account.getUuid());

        return ok();
    }

}
