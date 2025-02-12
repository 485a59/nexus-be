package cn.edu.nwafu.nexus.infrastructure.model.dto.recycles;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 批量删除回收文件 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "批量删除回收文件 DTO", required = true)
public class BatchDeleteRecoveryFileDto {
    @Schema(description = "用户文件 Id 集合", required = true)
    private List<String> ids;
}
