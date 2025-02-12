package cn.edu.nwafu.nexus.infrastructure.model.dto.recycles;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 回收文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "回收文件 DTO", required = true)
public class RestoreFileDto {
    @Schema(description = "删除批次号")
    private String deleteBatchNum;
    @Schema(description = "文件路径")
    private String path;
}
