package cn.edu.nwafu.nexus.infrastructure.model.vo.share;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分享列表响应。
 *
 * @author Huang Z.Y.
 */
@Schema(description = "分享列表VO")
@Data
public class ShareListVo {
    private String id;
    private String userId;
    private String shareTime;
    private String endTime;
    private String extractionCode;
    private String shareBatchNum;
    /**
     * 0 公共，1 私密，2 好友
     */
    private Integer shareType;
    /**
     * 0 正常，1 已失效，2 已撤销
     */
    private Integer shareStatus;
    private String shareFileId;
    private String userFileId;
    private String shareFilePath;
    private String fileId;
    private String fileName;
    private String filePath;
    private String extension;
    private Integer isDir;
    private String uploadTime;
    private Integer deleteFlag;
    private String url;
    private Long size;
    private Integer storageType;
}
