package cn.edu.nwafu.nexus.security.token;

import lombok.Data;

/**
 * 用户 Token。
 *
 * @author Huang Z.Y.
 */
@Data
public class UserToken {
    private String username;
    private String accessToken;
    private String refreshToken;
    private Long expires;
}
