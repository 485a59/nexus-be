package cn.edu.nwafu.nexus.ufop.operation.copy.support;

import cn.edu.nwafu.nexus.ufop.config.AliyunConfig;
import cn.edu.nwafu.nexus.ufop.operation.copy.Copier;
import cn.edu.nwafu.nexus.ufop.operation.copy.domain.CopyFile;
import cn.edu.nwafu.nexus.ufop.util.AliyunUtils;
import cn.edu.nwafu.nexus.ufop.util.UFOPUtils;
import com.aliyun.oss.OSS;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云 OSS 文件上传实现类。
 *
 * @author Huang Z.Y.
 */
public class AliyunOssCopier extends Copier {
    private AliyunConfig aliyunConfig;

    public AliyunOssCopier() {
    }

    public AliyunOssCopier(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    @Override
    public String copy(InputStream inputStream, CopyFile copyFile) {
        String uuid = UUID.randomUUID().toString();
        String fileUrl = UFOPUtils.getUploadFileUrl(uuid, copyFile.getExtension());
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        try {
            ossClient.putObject(aliyunConfig.getOss().getBucketName(), fileUrl, inputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
            ossClient.shutdown();
        }
        return fileUrl;
    }
}
