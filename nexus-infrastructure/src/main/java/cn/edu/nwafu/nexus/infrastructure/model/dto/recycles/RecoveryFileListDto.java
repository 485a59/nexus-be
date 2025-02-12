package cn.edu.nwafu.nexus.infrastructure.model.dto.recycles;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 回收文件列表 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "回收文件列表 DTO", required = true)
public class RecoveryFileListDto {
    @Schema(description = "当前页码")
    private Long pageNum;
    @Schema(description = "一页显示数量")
    private Long pageSize;
}
