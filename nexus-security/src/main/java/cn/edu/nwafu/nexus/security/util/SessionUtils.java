package cn.edu.nwafu.nexus.security.util;

import cn.edu.nwafu.nexus.security.domain.MemberDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 会话工具类。
 *
 * @author Huang Z.Y.
 */
public class SessionUtils {
    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof MemberDetails userDetails) {
            return userDetails.getUserId();
        }
        return null;
    }
}
