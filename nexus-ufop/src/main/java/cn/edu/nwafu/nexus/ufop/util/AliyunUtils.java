package cn.edu.nwafu.nexus.ufop.util;

import cn.edu.nwafu.nexus.ufop.config.AliyunConfig;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

/**
 * 阿里云工具类。
 *
 * @author Huang Z.Y.
 */
public class AliyunUtils {
    public static OSS getOSSClient(AliyunConfig aliyunConfig) {
        OSS ossClient = new OSSClientBuilder().build(aliyunConfig.getOss().getEndpoint(),
                aliyunConfig.getOss().getAccessKeyId(),
                aliyunConfig.getOss().getAccessKeySecret());
        return ossClient;
    }
}
