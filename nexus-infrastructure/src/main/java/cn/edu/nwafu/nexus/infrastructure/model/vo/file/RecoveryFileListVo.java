package cn.edu.nwafu.nexus.infrastructure.model.vo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 回收站列表响应。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "回收文件列表 VO", required = true)
public class RecoveryFileListVo {
    @Schema(description = "回收文件 id", example = "1")
    private String id;
    @Schema(description = "id", example = "1")
    private String userFileId;
    @Schema(description = "userId", example = "1")
    private String userId;
    @Schema(description = "fileId", example = "1")
    private String fileId;
    @Schema(description = "文件名", example = "图片")
    private String fileName;
    @Schema(description = "文件路径", example = "upload/bddd/caaa")
    private String filePath;
    @Schema(description = "文件大小", example = "1024")
    private Long size;
    @Schema(description = "文件扩展名", example = "zip")
    private String extension;
    @Schema(description = "是否是目录，1-是，0-否", example = "1")
    private Integer isDir;
    @Schema(description = "上传时间", example = "2020-10-10 12:21:22")
    private String uploadTime;
    @Schema(description = "删除标志", example = "1")
    private Integer deleteFlag;
    @Schema(description = "删除时间", example = "2020-10-10 12:21:22")
    private String deleteTime;
    @Schema(description = "删除批次号", example = "1111-222-22")
    private String deleteBatchNum;
}
