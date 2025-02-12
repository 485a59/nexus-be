package cn.edu.nwafu.nexus.ufop.operation.delete.support;

import cn.edu.nwafu.nexus.ufop.exception.operation.DeleteException;
import cn.edu.nwafu.nexus.ufop.operation.delete.Deleter;
import cn.edu.nwafu.nexus.ufop.operation.delete.domain.DeleteFile;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 本地文件删除实现类。
 *
 * @author Huang Z.Y.
 */
@Component
public class LocalStorageDeleter extends Deleter {
    @Override
    public void delete(DeleteFile deleteFile) {
        File localSaveFile = UFOPUtils.getLocalSaveFile(deleteFile.getFileUrl());
        if (localSaveFile.exists()) {
            boolean result = localSaveFile.delete();
            if (!result) {
                throw new DeleteException("删除本地文件失败");
            }
        }
        deleteCacheFile(deleteFile);
    }
}
