package cn.edu.nwafu.nexus.admin.service.impl;

import cn.edu.nwafu.nexus.admin.dto.LoginRequest;
import cn.edu.nwafu.nexus.admin.service.LoginService;
import cn.edu.nwafu.nexus.admin.service.TokenService;
import cn.edu.nwafu.nexus.common.exception.ApiException;
import cn.edu.nwafu.nexus.common.exception.error.ErrorCode;
import cn.edu.nwafu.nexus.common.util.i18n.MessageUtils;
import cn.edu.nwafu.nexus.infrastructure.user.admin.SystemUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 管理员登陆接口实现
 *
 * @author Huang Z.Y.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final TokenService tokenService;

    private final RedisService redisService;

    private final GuavaService guavaService;

    private final AuthenticationManager authenticationManager;

    @Override
    public String login(LoginRequest request) {
        Authentication authentication;
        String decryptPassword = decryptPassword(request.getPassword());
        try {
            // 该方法会去调用UserDetailsServiceImpl#loadUserByUsername  校验用户名和密码  认证鉴权
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(), decryptPassword));
        } catch (BadCredentialsException e) {
            ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(loginCommand.getUsername(), LoginStatusEnum.LOGIN_FAIL,
                    MessageUtils.message("Business.LOGIN_WRONG_USER_PASSWORD")));
            throw new ApiException(e, ErrorCode.Business.LOGIN_WRONG_USER_PASSWORD);
        } catch (AuthenticationException e) {
            ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(loginCommand.getUsername(), LoginStatusEnum.LOGIN_FAIL, e.getMessage()));
            throw new ApiException(e, ErrorCode.Business.LOGIN_ERROR, e.getMessage());
        } catch (Exception e) {
            ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(loginCommand.getUsername(), LoginStatusEnum.LOGIN_FAIL, e.getMessage()));
            throw new ApiException(e, ErrorCode.Business.LOGIN_ERROR, e.getMessage());
        }
        // 把当前登录用户放入上下文中
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 这里获取的loginUser是UserDetailsServiceImpl#loadUserByUsername方法返回的LoginUser
        SystemUserDetails loginUser = (SystemUserDetails) authentication.getPrincipal();
        recordLoginInfo(loginUser);
        // 生成token
        return tokenService.createTokenAndPutUserInCache(loginUser);
    }
}
