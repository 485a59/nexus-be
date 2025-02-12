package cn.edu.nwafu.nexus.infrastructure.model.vo.user;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class UpdateProfileDto {
    private String id;
    private Integer sex;
    private String nickName;
    private String phoneNumber;
    private String email;
}
