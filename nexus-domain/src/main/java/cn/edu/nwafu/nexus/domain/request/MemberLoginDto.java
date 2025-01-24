package cn.edu.nwafu.nexus.domain.request;

import lombok.Data;

/**
 * 用户登录请求体。
 *
 * @author Huang Z.Y.
 */
@Data
public class MemberLoginDto {
    private String username;
    private String password;
}
