package cn.edu.nwafu.nexus.ufop.operation.delete.support;

import cn.edu.nwafu.nexus.ufop.config.AliyunConfig;
import cn.edu.nwafu.nexus.ufop.operation.delete.Deleter;
import cn.edu.nwafu.nexus.ufop.operation.delete.domain.DeleteFile;
import cn.edu.nwafu.nexus.ufop.util.AliyunUtils;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import com.aliyun.oss.OSS;

/**
 * 阿里云文件删除实现类。
 *
 * @author Huang Z.Y.
 */
public class AliyunOssDeleter extends Deleter {
    private AliyunConfig aliyunConfig;

    public AliyunOssDeleter() {
    }

    public AliyunOssDeleter(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    @Override
    public void delete(DeleteFile deleteFile) {
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        try {
            ossClient.deleteObject(aliyunConfig.getOss().getBucketName(), UFOPUtils.getAliyunObjectNameByFileUrl(deleteFile.getFileUrl()));
        } finally {
            ossClient.shutdown();
        }
        deleteCacheFile(deleteFile);
    }
}

