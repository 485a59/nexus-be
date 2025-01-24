package cn.edu.nwafu.nexus.ufop.config;

import lombok.Data;

/**
 * MinIO 配置。
 *
 * @author Huang Z.Y.
 */
@Data
public class MinioConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
