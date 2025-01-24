package cn.edu.nwafu.nexus.ufop.domain;

import lombok.Data;

/**
 * 阿里云对象存储。
 *
 * @author Huang Z.Y.
 */
@Data
public class AliyunOss {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String objectName;
}
