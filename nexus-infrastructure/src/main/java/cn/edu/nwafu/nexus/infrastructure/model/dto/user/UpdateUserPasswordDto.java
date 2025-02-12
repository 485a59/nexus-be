package cn.edu.nwafu.nexus.infrastructure.model.dto.user;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class UpdateUserPasswordDto {
    private String id;
    private String newPassword;
    private String oldPassword;
}
