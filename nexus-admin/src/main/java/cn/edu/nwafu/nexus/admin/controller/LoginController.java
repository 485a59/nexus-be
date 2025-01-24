package cn.edu.nwafu.nexus.admin.controller;

import cn.edu.nwafu.nexus.admin.dto.LoginRequest;
import cn.edu.nwafu.nexus.admin.dto.LoginUserResult;
import cn.edu.nwafu.nexus.admin.dto.TokenResult;
import cn.edu.nwafu.nexus.admin.service.LoginService;
import cn.edu.nwafu.nexus.domain.service.AdminService;
import cn.edu.nwafu.nexus.infrastructure.user.AuthenticationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员登录控制器。
 *
 * @author Huang Z.Y.
 */
@Tag(name = "登录API", description = "登录相关接口")
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final AdminService userApplicationService;


    @Operation(summary = "登录")
    @PostMapping("/login")
    public R<TokenResult> login(@RequestBody LoginRequest param) {
        // 生成令牌
        String token = loginService.login(param);
        SystemUserDetails loginUser = AuthenticationUtils.getSystemUserDetails();
        LoginUserResult currentUserDTO = userApplicationService.getLoginUserInfo(loginUser);

        return R.ok(new TokenResult(token, currentUserDTO));
    }
}
