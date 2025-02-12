package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文件列表 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "文件列表 DTO", required = true)
public class FileListDto {
    @Schema(description = "文件路径", required = true)
    private String path;
    @Schema(description = "文件类型", required = true)
    private Integer type;
}
