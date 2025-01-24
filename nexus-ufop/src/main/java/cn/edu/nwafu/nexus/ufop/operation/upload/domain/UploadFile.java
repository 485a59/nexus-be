package cn.edu.nwafu.nexus.ufop.operation.upload.domain;

import lombok.Data;

/**
 * 切片上传相关参数。
 *
 * @author Huang Z.Y.
 */
@Data
public class UploadFile {
    private int chunkNumber;
    private long chunkSize;
    private int totalChunks;
    private String identifier;
    private long totalSize;
    private long currentChunkSize;
}

