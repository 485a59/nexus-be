package cn.edu.nwafu.nexus.infrastructure.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileSearch {
    private String indexName;
    private String userFileId;
    private String fileId;
    private String fileName;
    private String content;
    private String fileUrl;
    private Long fileSize;
    private Integer storageType;
    private String identifier;
    private Long userId;
    private String filePath;
    private String extendName;
    private Integer isDir;
    private String uploadTime;
    private Integer deleteFlag;
    private String deleteTime;
    private String deleteBatchNum;
}
