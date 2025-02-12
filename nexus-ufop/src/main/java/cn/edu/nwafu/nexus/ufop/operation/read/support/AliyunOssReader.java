package cn.edu.nwafu.nexus.ufop.operation.read.support;

import cn.edu.nwafu.nexus.ufop.config.AliyunConfig;
import cn.edu.nwafu.nexus.ufop.exception.operation.ReadException;
import cn.edu.nwafu.nexus.ufop.operation.read.Reader;
import cn.edu.nwafu.nexus.ufop.operation.read.domain.ReadFile;
import cn.edu.nwafu.nexus.ufop.util.AliyunUtils;
import cn.edu.nwafu.nexus.ufop.util.ReadFileUtils;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 阿里云文件读取实现类。
 *
 * @author Huang Z.Y.
 */
public class AliyunOssReader extends Reader {
    private AliyunConfig aliyunConfig;

    public AliyunOssReader() {
    }

    public AliyunOssReader(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    @Override
    public String read(ReadFile readFile) {
        String fileUrl = readFile.getFileUrl();
        String fileType = FilenameUtils.getExtension(fileUrl);
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        OSSObject ossObject = ossClient.getObject(aliyunConfig.getOss().getBucketName(),
                UFOPUtils.getAliyunObjectNameByFileUrl(fileUrl));
        InputStream inputStream = ossObject.getObjectContent();
        try {
            return ReadFileUtils.getContentByInputStream(fileType, inputStream);
        } catch (IOException e) {
            throw new ReadException("读取文件失败", e);
        } finally {
            ossClient.shutdown();
        }
    }

    public InputStream getInputStream(String fileUrl) {
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        OSSObject ossObject = ossClient.getObject(aliyunConfig.getOss().getBucketName(),
                UFOPUtils.getAliyunObjectNameByFileUrl(fileUrl));
        return ossObject.getObjectContent();
    }
}

