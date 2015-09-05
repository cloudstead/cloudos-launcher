package cloudos.launcher.dao;

import cloudos.launcher.model.Instance;
import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.server.LaunchApiConfiguration;
import org.cobbzilla.wizard.dao.UniquelyNamedEntityDAO;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;

import static org.cobbzilla.util.io.FileUtil.deleteOrDie;

@Repository public class InstanceDAO extends UniquelyNamedEntityDAO<Instance> {

    @Autowired private LaunchApiConfiguration configuration;

    @Override public Object preCreate(@Valid Instance instance) {
        instance.initUcid();
        return super.preCreate(instance);
    }

    public List<Instance> findByAccount(LaunchAccount account) {
        return findByField("adminUuid", account.getUuid());
    }

    public Instance findByNameAndAccount(LaunchAccount account, String name) {
        return uniqueResult(criteria().add(
                Restrictions.and(
                        Restrictions.eq("adminUuid", account.getUuid()),
                        Restrictions.eq("name", nameValue(name)))));
    }

    @Override public void delete(String uuid) {
        final Instance instance = findByUuid(uuid);
        if (instance == null) return;
        deleteOrDie(LaunchApiConfiguration.configDir(instance.getInitFilesDirName()));
        deleteOrDie(instance.getStagingDirFile());
        super.delete(uuid);
    }

    public void deleteByAccount(LaunchAccount account) {
        for (Instance instance : findByAccount(account)) {
            delete(instance.getUuid());
        }
    }
}
