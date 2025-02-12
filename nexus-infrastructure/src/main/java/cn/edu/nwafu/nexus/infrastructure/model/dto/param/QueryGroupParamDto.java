package cn.edu.nwafu.nexus.infrastructure.model.dto.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 获取组参数列表 DTO。
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(name = "获取组参数列表 DTO")
public class QueryGroupParamDto {
    @Schema(description = "组名")
    private String groupName;
}
