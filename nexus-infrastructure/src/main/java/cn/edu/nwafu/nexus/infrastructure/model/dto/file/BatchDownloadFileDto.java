package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 批量下载文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "批量下载文件 DTO", required = true)
public class BatchDownloadFileDto {
    @Schema(description = "文件集合", required = true)
    private List<String> ids;
    @Schema(description = "批次号")
    private String shareBatchNum;
    @Schema(description = "提取码")
    private String extractionCode;
}
