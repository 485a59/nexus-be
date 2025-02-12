package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分享列表请求对象。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "分享列表 DTO", required = true)
public class ShareListDto {
    @Schema(description = "分享文件路径")
    private String path;
    @Schema(description = "批次号")
    private String shareBatchNum;
    @Schema(description = "当前页码")
    private Long pageNum;
    @Schema(description = "一页显示数量")
    private Long pageSize;
}
