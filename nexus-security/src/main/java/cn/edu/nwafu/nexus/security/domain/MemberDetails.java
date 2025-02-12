package cn.edu.nwafu.nexus.security.domain;

import cn.edu.nwafu.nexus.infrastructure.model.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 用户登录详情。
 *
 * @author Huang Z.Y.
 */
@Getter
public class MemberDetails implements UserDetails {
    private final Member member;

    public MemberDetails(Member member) {
        this.member = member;
    }

    public String getUserId() {
        return member.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();

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
}
