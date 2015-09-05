package cloudos.launcher.dao;

import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.model.SshKey;
import org.cobbzilla.wizard.dao.UniquelyNamedEntityDAO;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public class SshKeyDAO extends UniquelyNamedEntityDAO<SshKey> {

    public List<SshKey> findByAccount(LaunchAccount account) {
        return findByField("account", account.getUuid());
    }

    public SshKey findByAccountAndName(LaunchAccount account, String name) {
        return uniqueResult(criteria().add(
                Restrictions.and(
                        Restrictions.eq("account", account.getUuid()),
                        Restrictions.or(
                                Restrictions.eq("name", nameValue(name)),
                                Restrictions.eq("uuid", nameValue(name))))));
    }

    public void deleteByAccount(LaunchAccount account) {
        for (SshKey key : findByAccount(account)) {
            delete(key.getUuid());
        }
    }

}
