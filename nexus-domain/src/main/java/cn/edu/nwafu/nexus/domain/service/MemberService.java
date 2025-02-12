package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.domain.response.MemberLoginVo;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Member;
import cn.edu.nwafu.nexus.infrastructure.model.vo.user.UserProfileVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户服务接口。
 *
 * @author Huang Z.Y.
 */
public interface MemberService extends IService<Member> {
    void register(String username, String password, String mobile, String authCode);

    MemberLoginVo login(String username, String password);

    UserDetails loadUserByUsername(String username);

    Member getByUsername(String username);

    String generateAuthCode(String telephone);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 获取当前用户信息
     */
    UserProfileVo getCurrentUserProfile();
}
