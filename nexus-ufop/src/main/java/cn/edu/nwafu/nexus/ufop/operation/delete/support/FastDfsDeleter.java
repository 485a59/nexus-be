package cn.edu.nwafu.nexus.ufop.operation.delete.support;

import cn.edu.nwafu.nexus.ufop.operation.delete.Deleter;
import cn.edu.nwafu.nexus.ufop.operation.delete.domain.DeleteFile;
import com.github.tobato.fastdfs.exception.FdfsServerException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FastDFS 文件删除实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Component
public class FastDfsDeleter extends Deleter {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Override
    public void delete(DeleteFile deleteFile) {
        try {
            fastFileStorageClient.deleteFile(deleteFile.getFileUrl().replace("M00", "group1"));
        } catch (FdfsServerException e) {
            log.error(e.getMessage());
        }
        deleteCacheFile(deleteFile);
    }
}