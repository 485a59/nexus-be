package cn.edu.nwafu.nexus.infrastructure.model.vo.share;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分享文件 VO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "分享文件 VO")
public class ShareFileVo {
    @Schema(description = "批次号")
    private String shareBatchNum;
    @Schema(description = "提取编码")
    private String extractionCode;
}
