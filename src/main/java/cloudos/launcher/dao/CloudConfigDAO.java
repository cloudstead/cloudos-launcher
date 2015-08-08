package cloudos.launcher.dao;

import cloudos.launcher.model.CloudConfig;
import cloudos.launcher.model.LaunchAccount;
import org.cobbzilla.wizard.dao.UniquelyNamedEntityDAO;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;

@Repository public class CloudConfigDAO extends UniquelyNamedEntityDAO<CloudConfig> {

    @Override public Object preCreate(@Valid CloudConfig cloud) { return cloud.encrypt(); }

    @Override public Object preUpdate(@Valid CloudConfig cloud) { return cloud.encrypt(); }

    public List<CloudConfig> findByAccount(LaunchAccount account) {
        final List<CloudConfig> configs = findByField("account", account.getUuid());
        for (CloudConfig c : configs) c.setLaunchAccount(account).decrypt();
        return configs;
    }

    public CloudConfig findByAccountAndName(LaunchAccount account, String name) {
        final CloudConfig config = uniqueResult(criteria().add(
                Restrictions.and(
                        Restrictions.eq("account", account.getUuid()),
                        Restrictions.eq("name", nameValue(name)))));
        return config == null ? null : config.setLaunchAccount(account).decrypt();
    }
}
