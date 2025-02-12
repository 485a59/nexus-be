package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 删除文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "删除文件 DTO", required = true)
public class DeleteFileDto {
    @Schema(description = "用户文件id", required = true)
    private String userFileId;
}
