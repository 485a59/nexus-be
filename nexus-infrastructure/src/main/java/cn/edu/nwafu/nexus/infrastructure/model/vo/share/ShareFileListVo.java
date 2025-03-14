package cn.edu.nwafu.nexus.infrastructure.model.vo.share;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分享文件列表响应对象。
 *
 * @author Huang Z.Y.
 */
@Schema(description = "分享文件列表 VO")
@Data
public class ShareFileListVo {
    @Schema(description = "文件id")
    private String fileId;
    @Schema(description = "文件时间戳姓名")
    private String timeStampName;
    @Schema(description = "文件url")
    private String fileUrl;
    @Schema(description = "文件大小")
    private Long fileSize;
    @Schema(description = "是否sso存储")
    @Deprecated
    private Integer isOSS;
    @Schema(description = "存储类型")
    private Integer storageType;
    @Schema(description = "用户文件id")
    private String userFileId;
    @Schema(description = "文件名")
    private String fileName;
    @Schema(description = "文件路径")
    private String filePath;
    @Schema(description = "文件扩展名")
    private String extendName;
    @Schema(description = "是否是目录 0-否， 1-是")
    private Integer isDir;
    @Schema(description = "上传时间")
    private String uploadTime;
    @Schema(description = "分享文件路径")
    private String shareFilePath;
    private String extractionCode;
    private String shareBatchNum;
}
