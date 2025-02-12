package cn.edu.nwafu.nexus.infrastructure.model.dto.commonfile;

import cn.edu.nwafu.nexus.infrastructure.model.entity.FilePermission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 共享文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "共享文件 DTO", required = true)
public class CommonFileDto {
    @Schema(name = "用户文件 id")
    private String id;
    @Schema(name = "共享用户 id 集合")
    private List<FilePermission> permissions;
}
