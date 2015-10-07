package cloudos.launcher.dao;

import cloudos.launcher.model.CloudConfig;
import cloudos.launcher.model.LaunchAccount;
import org.cobbzilla.wizard.dao.UniquelyNamedEntityDAO;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Transactional
@Repository public class CloudConfigDAO extends UniquelyNamedEntityDAO<CloudConfig> {

    @Override public Object preCreate(@Valid CloudConfig cloud) { return cloud.encrypt(); }

    @Override public Object preUpdate(@Valid CloudConfig cloud) { return cloud.encrypt(); }

    @Transactional(readOnly=true)
    public List<CloudConfig> findByAccount(LaunchAccount account) {
        final List<CloudConfig> configs = findByField("account", account.getUuid());
        for (CloudConfig c : configs) c.setLaunchAccount(account).decrypt();
        return configs;
    }

    @Transactional(readOnly=true)
    public CloudConfig findByAccountAndName(LaunchAccount account, String name) {
        final CloudConfig config = uniqueResult(criteria().add(
                Restrictions.and(
                        Restrictions.eq("account", account.getUuid()),
                        Restrictions.or(
                                Restrictions.eq("name", nameValue(name)),
                                Restrictions.eq("uuid", nameValue(name))))));
        return config == null ? null : config.setLaunchAccount(account).decrypt();
    }

    public void deleteByAccount(LaunchAccount account) {
        for (CloudConfig config : findByAccount(account)) {
            delete(config.getUuid());
        }
    }

}
