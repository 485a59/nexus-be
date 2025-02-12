package cn.edu.nwafu.nexus.infrastructure.model.dto.share;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分享类型 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "分享类型 DTO", required = true)
public class ShareTypeDto {
    @Schema(description = "批次号")
    private String shareBatchNum;
}
