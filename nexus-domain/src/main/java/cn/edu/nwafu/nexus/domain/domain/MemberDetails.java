package cn.edu.nwafu.nexus.domain.domain;

import cn.edu.nwafu.nexus.infrastructure.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户登录详情。
 *
 * @author Huang Z.Y.
 */
public class MemberDetails implements UserDetails {
    private final Member member;

    public MemberDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 创建一个空的权限集合
        List<GrantedAuthority> authorities = new ArrayList<>();
//        Set<String> roles = member.getRoles();
//        if (roles != null && !roles.isEmpty()) {
//            roles.stream()
//                    .map(SimpleGrantedAuthority::new)
//                    .forEach(authorities::add);
//        }
//
//        // 获取 permissions 字段
//        Set<String> permissions = member.getPermissions();
//        if (permissions != null && !permissions.isEmpty()) {
//            permissions.stream()
//                    .map(SimpleGrantedAuthority::new)
//                    .forEach(authorities::add);
//        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Member getMember() {
        return member;
    }
}
