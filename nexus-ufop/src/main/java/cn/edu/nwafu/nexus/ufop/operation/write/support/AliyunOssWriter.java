package cn.edu.nwafu.nexus.ufop.operation.write.support;

import cn.edu.nwafu.nexus.ufop.config.AliyunConfig;
import cn.edu.nwafu.nexus.ufop.operation.write.Writer;
import cn.edu.nwafu.nexus.ufop.operation.write.domain.WriteFile;
import cn.edu.nwafu.nexus.ufop.util.AliyunUtils;
import cn.edu.nwafu.nexus.ufop.util.UFOPUtils;
import com.aliyun.oss.OSS;

import java.io.InputStream;

/**
 * 阿里云文件写入实现类。
 *
 * @author Huang Z.Y.
 */
public class AliyunOssWriter extends Writer {

    private AliyunConfig aliyunConfig;

    public AliyunOssWriter() {

    }

    public AliyunOssWriter(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    @Override
    public void write(InputStream inputStream, WriteFile writeFile) {
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);

        ossClient.putObject(aliyunConfig.getOss().getBucketName(), UFOPUtils.getAliyunObjectNameByFileUrl(writeFile.getFileUrl()), inputStream);
        ossClient.shutdown();
    }
}

