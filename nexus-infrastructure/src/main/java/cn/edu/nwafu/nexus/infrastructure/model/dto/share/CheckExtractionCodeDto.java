package cn.edu.nwafu.nexus.infrastructure.model.dto.share;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 校验提取码 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "校验提取码 DTO", required = true)
public class CheckExtractionCodeDto {
    @Schema(description = "批次号")
    private String shareBatchNum;
    @Schema(description = "提取码")
    private String extractionCode;
}
