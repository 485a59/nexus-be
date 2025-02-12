package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 复制文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "复制文件 DTO", required = true)
public class CopyFileDto {
    @Schema(description = "用户文件id集合", required = true)
    private List<String> ids;

    @Schema(description = "文件路径", required = true)
    private String path;
}
