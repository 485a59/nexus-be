package cn.edu.nwafu.nexus.ufop.operation.delete.support;

import cn.edu.nwafu.nexus.ufop.config.QiniuyunConfig;
import cn.edu.nwafu.nexus.ufop.exception.operation.DeleteException;
import cn.edu.nwafu.nexus.ufop.operation.delete.Deleter;
import cn.edu.nwafu.nexus.ufop.operation.delete.domain.DeleteFile;
import cn.edu.nwafu.nexus.ufop.util.QiniuyunUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;

/**
 * 七牛云删除文件实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class QiniuyunKodoDeleter extends Deleter {
    private QiniuyunConfig qiniuyunConfig;

    public QiniuyunKodoDeleter() {
    }

    public QiniuyunKodoDeleter(QiniuyunConfig qiniuyunConfig) {
        this.qiniuyunConfig = qiniuyunConfig;
    }

    @Override
    public void delete(DeleteFile deleteFile) {
        Configuration cfg = QiniuyunUtils.getCfg(qiniuyunConfig);
        Auth auth = Auth.create(qiniuyunConfig.getKodo().getAccessKey(), qiniuyunConfig.getKodo().getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(qiniuyunConfig.getKodo().getBucketName(), deleteFile.getFileUrl());
        } catch (QiniuException ex) {
            throw new DeleteException("七牛云删除文件失败", ex);
        }
        deleteCacheFile(deleteFile);
    }
}
