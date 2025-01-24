package cn.edu.nwafu.nexus.admin.service;

import cn.edu.nwafu.nexus.admin.dto.LoginRequest;

/**
 * 管理员登录接口。
 *
 * @author Huang Z.Y.
 */
public interface LoginService {
    /**
     * 登陆验证。
     *
     * @param request 登陆参数
     * @return 结果
     */
    String login(LoginRequest request);
}
