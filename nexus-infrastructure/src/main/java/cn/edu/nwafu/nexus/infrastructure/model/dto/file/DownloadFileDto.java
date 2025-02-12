package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 下载文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "下载文件 DTO", required = true)
public class DownloadFileDto {
    private String id;
    @Schema(description = "批次号")
    private String shareBatchNum;
    @Schema(description = "提取码")
    private String extractionCode;
}
