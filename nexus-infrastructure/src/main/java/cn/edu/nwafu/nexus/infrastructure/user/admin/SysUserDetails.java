package cn.edu.nwafu.nexus.infrastructure.user.admin;

import cn.edu.nwafu.nexus.infrastructure.user.BaseUserDetails;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

/**
 * 登录管理员身份权限
 *
 * @author Huang Z.Y.
 */
@Data
@NoArgsConstructor
public class SysUserDetails extends BaseUserDetails {
    @Serial
    private static final long serialVersionUID = 43557723910L;

    private boolean isAdmin;

    @Setter
    private RoleDetails roleDetails;

    /**
     * 当超过这个时间，则触发刷新缓存时间
     */
    private Long autoRefreshCacheTime;


    public SysUserDetails(Long userId, Boolean isAdmin, String username, String password, RoleDetails roleDetails) {
        this.userId = userId;
        this.isAdmin = isAdmin;
        this.username = username;
        this.password = password;
        this.roleDetails = roleDetails;
    }
}
