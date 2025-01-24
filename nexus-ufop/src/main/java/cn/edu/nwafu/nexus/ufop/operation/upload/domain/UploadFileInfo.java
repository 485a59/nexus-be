package cn.edu.nwafu.nexus.ufop.operation.upload.domain;

import lombok.Data;

/**
 * 上传文件信息。
 *
 * @author Huang Z.Y.
 */
@Data
public class UploadFileInfo {
    private String bucketName;
    private String key;
    private String uploadId;
}