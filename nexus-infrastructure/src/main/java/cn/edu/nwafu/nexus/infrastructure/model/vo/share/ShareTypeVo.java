package cn.edu.nwafu.nexus.infrastructure.model.vo.share;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分享类型 VO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "分享类型 VO")
public class ShareTypeVo {
    @Schema(description = "0公共，1私密，2好友")
    private Integer shareType;
}
