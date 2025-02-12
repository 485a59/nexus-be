package cn.edu.nwafu.nexus.domain.response;

import cn.edu.nwafu.nexus.infrastructure.model.entity.Image;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Music;
import lombok.Data;

import java.util.Date;

/**
 * 文件详情响应体。
 *
 * @author Huang Z.Y.
 */
@Data
public class FileDetailVo {
    private String fileId;
    private Date createTime;
    private String url;
    private Long size;
    private Integer storageType;
    private Integer pointCount;
    private String identifier;
    private String id;
    private String userId;
    private String fileName;
    private String filePath;
    private String extension;
    private Integer isDir;
    private String uploadTime;
    private String deleteTime;
    private String deleteBatchNum;
    private Image image;
    private Music music;
}
