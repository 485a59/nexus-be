package cn.edu.nwafu.nexus.infrastructure.model.dto.share;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 分享文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "分享文件 DTO", required = true)
public class ShareFileDto {
    @Schema(description = "文件ID集合", required = true)
    @NotEmpty
    private List<String> ids;
    @Schema(description = "过期日期", example = "2020-05-23 22:10:33")
    private String endTime;
    @Schema(description = "分享类型", example = "0 公共分享，1 私密分享，2 好友分享")
    private Integer shareType;
    @Schema(description = "备注", example = "")
    private String remarks;
    @Schema(description = "分享码")
    private String extractionCode;
}
