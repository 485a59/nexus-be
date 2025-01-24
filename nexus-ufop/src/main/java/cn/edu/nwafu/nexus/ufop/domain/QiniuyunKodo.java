package cn.edu.nwafu.nexus.ufop.domain;

import lombok.Data;

/**
 * 七牛云对象存储。
 *
 * @author Huang Z.Y.
 */
@Data
public class QiniuyunKodo {
    private String domain;
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
