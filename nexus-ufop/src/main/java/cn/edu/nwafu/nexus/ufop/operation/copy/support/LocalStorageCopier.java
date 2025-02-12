package cn.edu.nwafu.nexus.ufop.operation.copy.support;

import cn.edu.nwafu.nexus.ufop.exception.operation.CopyException;
import cn.edu.nwafu.nexus.ufop.operation.copy.Copier;
import cn.edu.nwafu.nexus.ufop.operation.copy.domain.CopyFile;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 本地文件上传实现类。
 *
 * @author Huang Z.Y.
 */
public class LocalStorageCopier extends Copier {
    @Override
    public String copy(InputStream inputStream, CopyFile copyFile) {
        String uuid = UUID.randomUUID().toString();
        String fileUrl = UFOPUtils.getUploadFileUrl(uuid, copyFile.getExtension());
        File saveFile = new File(UFOPUtils.getStaticPath() + fileUrl);
        try {
            FileUtils.copyInputStreamToFile(inputStream, saveFile);
        } catch (IOException e) {
            throw new CopyException("创建文件出现异常", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return fileUrl;
    }
}
