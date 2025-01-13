package cn.edu.nwafu.nexus.infrastructure.user;

import cn.edu.nwafu.nexus.common.api.ResultCode;
import cn.edu.nwafu.nexus.common.exception.ApiException;
import cn.edu.nwafu.nexus.infrastructure.user.admin.SysUserDetails;
import cn.edu.nwafu.nexus.infrastructure.user.app.AppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Huang Z.Y.
 */
public class AuthenticationUtils {
    /**
     * 用户ID
     **/
    public static Long getUserId() {
        try {
            return getSysUserDetails().getUserId();
        } catch (Exception e) {
            throw new ApiException(ResultCode.Business.USER_FAIL_TO_GET_USER_ID);
        }
    }

    /**
     * 获取系统用户
     **/
    public static SysUserDetails getSysUserDetails() throws ApiException {
        try {
            return (SysUserDetails) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ApiException(ResultCode.Business.USER_FAIL_TO_GET_USER_INFO);
        }
    }

    /**
     * 获取App用户
     **/
    public static AppUserDetails getAppUserDetails() {
        try {
            return (AppUserDetails) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ApiException(ResultCode.Business.USER_FAIL_TO_GET_USER_INFO);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
