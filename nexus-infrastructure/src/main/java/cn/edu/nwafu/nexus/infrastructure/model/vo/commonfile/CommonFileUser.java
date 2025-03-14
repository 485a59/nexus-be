package cn.edu.nwafu.nexus.infrastructure.model.vo.commonfile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class CommonFileUser {
    @Schema(description = "用户id", example = "1")
    private long userId;
    @Schema(description = "用户名", example = "奇文网盘")
    private String username;
}
