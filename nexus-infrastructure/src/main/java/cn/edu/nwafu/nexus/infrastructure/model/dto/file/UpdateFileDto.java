package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "修改文件 DTO", required = true)
public class UpdateFileDto {
    @Schema(description = "用户文件 id")
    private String id;
    @Schema(description = "文件内容")
    private String content;
}
