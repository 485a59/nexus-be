package cn.edu.nwafu.nexus.infrastructure.model.dto.share;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分享文件列表 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "分享文件列表 DTO", required = true)
public class ShareFileListDto {
    @Schema(description = "批次号")
    private String shareBatchNum;
    @Schema(description = "分享文件路径")
    private String shareFilePath;
}
