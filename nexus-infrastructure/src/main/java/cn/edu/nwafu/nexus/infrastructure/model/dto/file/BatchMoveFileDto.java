package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 批量移动文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "批量移动文件 DTO", required = true)
public class BatchMoveFileDto {
    @Schema(description = "用户文件 Id 集合", required = true)
    private List<String> ids;
    @Schema(description = "目的文件路径", required = true)
    private String path;
}
