package cn.edu.nwafu.nexus.domain.request;

import lombok.Data;

/**
 * 用户注册请求体。
 *
 * @author Huang Z.Y.
 */
@Data
public class MemberRegisterDto {
    private String username;
    private String password;
    private String telephone;
    private String authCode;
}
