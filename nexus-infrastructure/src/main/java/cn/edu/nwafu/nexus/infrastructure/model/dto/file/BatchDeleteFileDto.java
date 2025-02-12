package cn.edu.nwafu.nexus.infrastructure.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 批量删除文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "批量删除文件 DTO", required = true)
public class BatchDeleteFileDto {
    @Schema(description = "文件 Id 集合", required = true)
    @NotEmpty
    private List<String> ids;
}
