package cn.edu.nwafu.nexus.infrastructure.model.dto.share;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 保存分享文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "保存分享文件 DTO", required = true)
public class SaveShareFileDto {
    @Schema(description = "用户文件id集合", required = true)
    private List<String> ids;
    @Schema(description = "文件路径", required = true)
    private String path;
}
