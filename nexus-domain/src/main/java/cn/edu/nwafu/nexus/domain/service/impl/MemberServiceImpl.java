package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.common.exception.Asserts;
import cn.edu.nwafu.nexus.domain.response.MemberLoginVo;
import cn.edu.nwafu.nexus.domain.service.MemberCacheService;
import cn.edu.nwafu.nexus.domain.service.MemberService;
import cn.edu.nwafu.nexus.infrastructure.mapper.MemberMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.SysDeptMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.SysRoleMapper;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Member;
import cn.edu.nwafu.nexus.infrastructure.model.entity.SysRole;
import cn.edu.nwafu.nexus.infrastructure.model.vo.user.UserProfileVo;
import cn.edu.nwafu.nexus.security.domain.MemberDetails;
import cn.edu.nwafu.nexus.security.dto.UserToken;
import cn.edu.nwafu.nexus.security.util.JwtTokenUtils;
import cn.edu.nwafu.nexus.security.util.SessionUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Huang Z.Y.
 */
@Service
@Slf4j
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
    @Value("${redis.key.auth-code}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.expire.auth-code}")
    private Long AUTH_CODE_EXPIRE_SECONDS;
    @Value("${jwt.expiration}")
    private Long EXPIRE_SECONDS;

    @Resource
    private MemberCacheService memberCacheService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private JwtTokenUtils jwtTokenUtils;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public void register(String username, String password, String telephone, String authCode) {
        // 验证验证码
        if (!verifyAuthCode(authCode, telephone)) {
            Asserts.fail("验证码错误");
        }
        // 查询是否已有该用户
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .or()
                .eq("phone_number", telephone);
        List<Member> members = memberMapper.selectList(queryWrapper);

        if (CollUtil.isNotEmpty(members)) {
            Asserts.fail("该用户已存在");
        }
        // 没有该用户进行添加操作
        Member member = new Member();
        member.setUsername(username);
        member.setPhoneNumber(telephone);
        // 密码加密
        member.setPassword(passwordEncoder.encode(password));
        member.setLoginTime(new Date());
        // 插入新用户
        memberMapper.insert(member);
        // 清空密码，避免返回敏感信息
        member.setPassword(null);
    }

    @Override
    public MemberLoginVo login(String username, String password) {
        try {
            // 加载用户信息
            UserDetails userDetails = loadUserByUsername(username);
            // 验证密码
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            // 创建认证对象并设置到 SecurityContext 中
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 生成 Token
            UserToken userToken = jwtTokenUtils.generateTokens(userDetails);
            // 设置 Token 的过期时间
            long currentTimeMillis = System.currentTimeMillis();
            userToken.setExpires(currentTimeMillis + EXPIRE_SECONDS * 1000);
            System.out.println("userToken:" + userToken);
            MemberLoginVo loginVo = new MemberLoginVo(userToken);
            // 如果 UserDetails 是 MemberDetails 类型，获取角色信息
            if (userDetails instanceof MemberDetails memberDetails) {
                Member member = memberDetails.getMember();
                loginVo.setAvatar(member.getAvatar());
                loginVo.setNickname(member.getNickname());
                List<SysRole> roles = memberMapper.getRolesByUserId(member.getId());
                // 根据用户的 role_id 查询对应的角色信息
                if (CollUtil.isNotEmpty(roles)) {
                    loginVo.setRoles(roles.stream().map(SysRole::getRoleKey).collect(Collectors.toList()));
                } else {
                    loginVo.setRoles(new ArrayList<>());
                }
            }
            return loginVo;
        } catch (AuthenticationException e) {
            log.warn("登录异常: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = getByUsername(username);
        if (member != null) {
            return new MemberDetails(member);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public Member getByUsername(String username) {
        Member member = memberCacheService.getMember(username);
        if (member != null) {
            return member;
        }
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        member = memberMapper.selectOne(queryWrapper);

        // 如果查询到用户信息，则将其放入缓存
        if (member != null) {
            memberCacheService.setMember(member);
        }
        return member;
    }

    @Override
    public String generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        memberCacheService.setAuthCode(telephone, sb.toString());
        return sb.toString();
    }

    private boolean verifyAuthCode(String authCode, String email) {
        if (StrUtil.isEmpty(authCode)) {
            return true;
        }
        String realAuthCode = memberCacheService.getAuthCode(email);
        return authCode.equals(realAuthCode);
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public UserProfileVo getCurrentUserProfile() {
        String userId = SessionUtils.getUserId();
        Member member = getById(userId);
        UserProfileVo profileVo = new UserProfileVo();
        BeanUtils.copyProperties(member, profileVo);
        return profileVo;
    }
}
