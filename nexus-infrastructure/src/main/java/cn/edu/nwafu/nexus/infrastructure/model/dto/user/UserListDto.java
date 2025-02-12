package cn.edu.nwafu.nexus.infrastructure.model.dto.user;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class UserListDto {
    private Integer deptId;
    private String username;
    private Integer status;
    private String phoneNumber;
}
