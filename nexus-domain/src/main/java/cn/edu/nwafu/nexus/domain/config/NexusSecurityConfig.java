package cn.edu.nwafu.nexus.domain.config;

import cn.edu.nwafu.nexus.domain.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

/**
 * @author Huang Z.Y.
 */
@Configuration
public class NexusSecurityConfig {
    @Resource
    private MemberService memberService;

    @Bean
    public UserDetailsService userDetailsService() {
        // 获取登录用户信息
        return username -> memberService.loadUserByUsername(username);
    }
}
