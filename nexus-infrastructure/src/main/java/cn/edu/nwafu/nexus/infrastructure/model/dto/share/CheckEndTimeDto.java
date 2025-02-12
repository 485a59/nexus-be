package cn.edu.nwafu.nexus.infrastructure.model.dto.share;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 校验过期时间 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "校验过期时间 DTO", required = true)
public class CheckEndTimeDto {
    @Schema(description = "批次号")
    private String shareBatchNum;
}
