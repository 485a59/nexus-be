package cn.edu.nwafu.nexus.infrastructure.model.dto.system;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class LoginLogDto {
    private String ipAddress;
    private Integer status;
    private String username;
}
