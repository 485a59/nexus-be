package cn.edu.nwafu.nexus.ufop.operation.delete;

import cn.edu.nwafu.nexus.ufop.operation.delete.domain.DeleteFile;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

/**
 * 文件删除抽象类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public abstract class Deleter {
    public abstract void delete(DeleteFile deleteFile);

    protected void deleteCacheFile(DeleteFile deleteFile) {
        if (UFOPUtils.isImageFile(FilenameUtils.getExtension(deleteFile.getFileUrl()))) {
            File cacheFile = UFOPUtils.getCacheFile(deleteFile.getFileUrl());
            if (cacheFile.exists()) {
                boolean result = cacheFile.delete();
                if (!result) {
                    log.error("删除本地缓存文件失败");
                }
            }
        }
    }
}
