package cn.edu.nwafu.nexus.infrastructure.user;

import lombok.Data;

/**
 * 用户登陆信息，用于记录登陆日志
 *
 * @author Huang Z.Y.
 */
@Data
public class LoginDetails {
    /**
     * 登录IP地址
     */
    private String ipAddress;
    /**
     * 登录地点
     */
    private String location;
    /**
     * 浏览器类型
     */
    private String browser;
    /**
     * 操作系统
     */
    private String operationSystem;
    /**
     * 登录时间
     */
    private Long loginTime;
}
