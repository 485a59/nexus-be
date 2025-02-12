package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 预览文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Builder
@Schema(name = "预览文件 DTO", required = true)
public class PreviewDto {
    private String id;
    @Schema(description = "批次号")
    private String shareBatchNum;
    @Schema(description = "提取码")
    private String extractionCode;
    private String isMin;
    private Integer platform;
    private String url;
    private String token;
}
