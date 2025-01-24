package cn.edu.nwafu.nexus.admin.dto;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class LoginRequest {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 验证码
     */
    private String captchaCode;
    /**
     * 唯一标识
     */
    private String captchaCodeKey;
}
