package cn.edu.nwafu.nexus.domain.response;

import cn.edu.nwafu.nexus.infrastructure.entity.Image;
import cn.edu.nwafu.nexus.infrastructure.entity.Music;
import lombok.Data;

/**
 * 文件详情响应体。
 *
 * @author Huang Z.Y.
 */
@Data
public class FileDetailVo {
    private String fileId;
    private String timeStampName;
    private String fileUrl;
    private Long fileSize;
    private Integer storageType;
    private Integer pointCount;
    private String identifier;
    private String userFileId;
    private Long userId;
    private String fileName;
    private String filePath;
    private String extendName;
    private Integer isDir;
    private String uploadTime;
    private Integer deleted;
    private String deleteTime;
    private String deleteBatchNum;
    private Image image;
    private Music music;
}
