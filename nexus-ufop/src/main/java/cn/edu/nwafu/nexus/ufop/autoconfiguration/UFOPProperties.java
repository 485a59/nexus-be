package cn.edu.nwafu.nexus.ufop.autoconfiguration;

import cn.edu.nwafu.nexus.ufop.config.AliyunConfig;
import cn.edu.nwafu.nexus.ufop.config.MinioConfig;
import cn.edu.nwafu.nexus.ufop.config.QiniuyunConfig;
import cn.edu.nwafu.nexus.ufop.domain.ThumbImage;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * UFOP 配置项。
 *
 * @author Huang Z.Y.
 */
@Data
@ConfigurationProperties(prefix = "ufop")
public class UFOPProperties {
    private String bucketName;
    private String storageType;
    private String localStoragePath;
    private AliyunConfig aliyun = new AliyunConfig();
    private ThumbImage thumbImage = new ThumbImage();
    private MinioConfig minio = new MinioConfig();
    private QiniuyunConfig qiniuyun = new QiniuyunConfig();
}

