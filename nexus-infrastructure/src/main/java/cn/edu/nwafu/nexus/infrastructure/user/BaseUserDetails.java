package cn.edu.nwafu.nexus.infrastructure.user;

import cn.edu.nwafu.nexus.common.util.context.ServletHolderUtils;
import cn.edu.nwafu.nexus.common.util.ip.IpRegionUtils;
import cn.hutool.extra.servlet.ServletUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 登录用户身份权限
 *
 * @author Huang Z.Y.
 */
@Data
@NoArgsConstructor
public class BaseUserDetails implements UserDetails {
    @Serial
    private static final long serialVersionUID = 3424892301234L;
    protected LoginDetails loginDetails;
    protected Long userId;
    /**
     * 用户唯一标识，缓存的key
     */
    protected String cachedKey;
    protected String username;
    protected String password;
    protected List<GrantedAuthority> authorities = new ArrayList<>();

    public BaseUserDetails(Long userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public void setLoginDetails() {
        this.loginDetails = new LoginDetails();
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletHolderUtils.getRequest().getHeader("User-Agent"));
        String ip = ServletUtil.getClientIP(ServletHolderUtils.getRequest());

        this.loginDetails.setIpAddress(ip);
        this.loginDetails.setLocation(IpRegionUtils.getBriefLocationByIp(ip));
        this.loginDetails.setBrowser(userAgent.getBrowser().getName());
        this.loginDetails.setOperationSystem(userAgent.getOperatingSystem().getName());
        this.loginDetails.setLoginTime(System.currentTimeMillis());
    }

    public void grantPermission(String appName) {
        authorities.add(new SimpleGrantedAuthority(appName));
    }


    @Override
    public String getUsername() {
        return this.username;
    }


    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * 账户是否未过期，过期无法验证
     * 未实现此功能
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁，锁定的用户无法进行身份验证
     * 未实现此功能
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码)，过期的凭据防止认证
     * 未实现此功能
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用，禁用的用户不能身份验证
     * 未实现此功能
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
