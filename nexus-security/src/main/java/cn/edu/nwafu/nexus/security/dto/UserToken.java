package cn.edu.nwafu.nexus.security.dto;

import lombok.Data;

/**
 * 用户 Token。
 *
 * @author Huang Z.Y.
 */
@Data
public class UserToken {
    protected String username;
    protected String accessToken;
    protected String refreshToken;
    protected Long expires;
}
