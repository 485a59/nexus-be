package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Huang Z.Y.
 */
@Data
public class CreateFolderDto {
    @Schema(description = "文件名", required = true)
    @NotBlank(message = "文件名不能为空")
    private String name;
    @Schema(description = "文件路径", required = true)
    private String path;
}
