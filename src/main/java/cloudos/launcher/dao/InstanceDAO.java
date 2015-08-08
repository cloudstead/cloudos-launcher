package cloudos.launcher.dao;

import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchAccount;
import org.cobbzilla.wizard.dao.UniquelyNamedEntityDAO;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public class InstanceDAO extends UniquelyNamedEntityDAO<Instance> {

    public List<Instance> findByAccount(LaunchAccount account) {
        return findByField("account", account.getUuid());
    }

    public Instance findByNameAndAccount(LaunchAccount account, String name) {
        return uniqueResult(criteria().add(
                Restrictions.and(
                        Restrictions.eq("account", account.getUuid()),
                        Restrictions.eq("name", nameValue(name)))));
    }
}
