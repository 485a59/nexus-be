package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 重命名文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "重命名文件 DTO", required = true)
public class RenameFileDto {
    @Schema(description = "用户文件 id", required = true)
    private String id;

    @Schema(description = "文件名", required = true)
    private String name;
}
