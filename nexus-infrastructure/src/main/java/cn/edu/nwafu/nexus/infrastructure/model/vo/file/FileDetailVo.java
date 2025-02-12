package cn.edu.nwafu.nexus.infrastructure.model.vo.file;

import cn.edu.nwafu.nexus.infrastructure.model.entity.Image;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Music;

/**
 * @author Huang Z.Y.
 */
public class FileDetailVo {
    private String id;
    private String timeStampName;
    private String url;
    private Long size;
    private Integer storageType;
    private Integer pointCount;
    private String identifier;
    private String userFileId;
    private Long userId;
    private String name;
    private String path;
    private String extension;
    private Integer isDir;
    private String uploadTime;
    private String deleteTime;
    private String deleteBatchNum;
    private Image image;
    private Music music;
}
