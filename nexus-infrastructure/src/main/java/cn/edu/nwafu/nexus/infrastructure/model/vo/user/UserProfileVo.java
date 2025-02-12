package cn.edu.nwafu.nexus.infrastructure.model.vo.user;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class UserProfileVo {
    private String id;
    private String username;
    private String nickname;
    private String email;
    private String phoneNumber;
    private String avatar;
}
