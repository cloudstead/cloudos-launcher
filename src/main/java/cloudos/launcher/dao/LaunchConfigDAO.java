package cloudos.launcher.dao;

import cloudos.launcher.model.LaunchAccount;
import cloudos.launcher.model.LaunchConfig;
import org.cobbzilla.wizard.dao.UniquelyNamedEntityDAO;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.io.File;
import java.util.List;

import static org.cobbzilla.util.daemon.ZillaRuntime.die;
import static org.cobbzilla.util.io.FileUtil.abs;

@Repository
public class LaunchConfigDAO extends UniquelyNamedEntityDAO<LaunchConfig> {

    @Override
    public LaunchConfig findByName(String name) {
        final LaunchConfig found = super.findByName(name);
        if (found == null) return null;
        return found.readZipData();
    }

    @Override
    public Object preCreate(@Valid LaunchConfig config) {
        config.writeZipData();
        return super.preCreate(config);
    }

    @Override
    public Object preUpdate(@Valid LaunchConfig config) {
        config.writeZipData();
        return super.preUpdate(config);
    }

    @Override
    public void delete(String uuid) {
        final LaunchConfig config = findByUuid(uuid);
        if (config == null) die("not found: "+uuid);
        final File zipFile = config.getZipFile();
        if (!zipFile.delete()) die("Error deleting zipFile: "+abs(zipFile));
        super.delete(uuid);
    }

    public List<LaunchConfig> findByAccount(LaunchAccount account) {
        final List<LaunchConfig> configs = findByField("account", account.getUuid());
        for (LaunchConfig c : configs) {
            c.setLaunchAccount(account).readZipData();
        }
        return configs;
    }

    public LaunchConfig findByAccountAndName(LaunchAccount account, String name) {
        final LaunchConfig config = uniqueResult(criteria().add(
                Restrictions.and(
                        Restrictions.eq("account", account.getUuid()),
                        Restrictions.eq("name", nameValue(name)))));
        return config == null ? null : config.setLaunchAccount(account).readZipData();
    }
}
