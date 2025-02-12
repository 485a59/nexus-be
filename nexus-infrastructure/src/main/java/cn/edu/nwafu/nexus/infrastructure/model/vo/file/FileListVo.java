package cn.edu.nwafu.nexus.infrastructure.model.vo.file;

import lombok.Data;

import java.util.Date;

/**
 * @author Huang Z.Y.
 */
@Data
public class FileListVo {
    private String id;
    private String userId;
    private String fileId;
    private String fileName;
    private String filePath;
    private String extension;
    private Integer isDir;
    private Date uploadTime;
    private String deleteBatchNum;
    private String createUserId;
    private String modifyUserId;
}
