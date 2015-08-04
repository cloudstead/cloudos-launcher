package cloudos.launcher.dao;

import cloudos.launcher.model.LaunchConfig;
import cloudos.launcher.server.LaunchApiConfiguration;
import lombok.Cleanup;
import org.apache.commons.compress.utils.IOUtils;
import org.cobbzilla.util.io.FileUtil;
import org.cobbzilla.util.string.Base64;
import org.cobbzilla.wizard.dao.UniquelyNamedEntityDAO;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.io.*;

import static org.cobbzilla.util.daemon.ZillaRuntime.die;
import static org.cobbzilla.util.io.FileUtil.abs;

@Repository
public class LaunchConfigDAO extends UniquelyNamedEntityDAO<LaunchConfig> {

    public static final String ZIP_FILE_DIR = "configs";

    protected File getZipFileDir () { return LaunchApiConfiguration.configDir(ZIP_FILE_DIR); }

    private File getZipFile(String uuid) {
        return new File(getZipFileDir(), "cloudos-launch-config-"+uuid+".zip");
    }

    public String readZipData(String uuid) {
        final File zipFile = getZipFile(uuid);
        try {
            @Cleanup final InputStream in = new FileInputStream(zipFile);
            @Cleanup final ByteArrayOutputStream out = new ByteArrayOutputStream((int) (zipFile.length() + (zipFile.length() / 3)));
            IOUtils.copy(in, out);
            return Base64.encodeBytes(out.toByteArray());

        } catch (Exception e) {
            die("readZipData: "+e, e);
        }
        return FileUtil.toStringOrDie(zipFile);
    }

    protected void writeZipData(@Valid LaunchConfig entity) {
        if (!entity.hasBase64zipData()) die("writeZipData: no base64zipData provided");
        final File zipFile = getZipFile(entity.getUuid());
        try {
            @Cleanup final InputStream in = new ByteArrayInputStream(Base64.decode(entity.getBase64zipData()));
            @Cleanup final OutputStream out = new FileOutputStream(zipFile);
            IOUtils.copy(in, out);
        } catch (Exception e) {
            die("preCreate: error writing base64zipData to disk: "+e, e);
        }
    }

    @Override
    public LaunchConfig findByName(String name) {
        final LaunchConfig found = super.findByName(name);
        if (found == null) return null;
        return found.setBase64zipData(readZipData(found.getUuid()));
    }

    @Override
    public Object preCreate(@Valid LaunchConfig entity) {
        writeZipData(entity);
        return super.preCreate(entity);
    }

    @Override
    public Object preUpdate(@Valid LaunchConfig entity) {
        writeZipData(entity);
        return super.preUpdate(entity);
    }

    @Override
    public void delete(String uuid) {
        final File zipFile = getZipFile(uuid);
        if (!zipFile.delete()) die("Error deleting zipFile: "+abs(zipFile));
        super.delete(uuid);
    }
}
