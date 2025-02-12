package cn.edu.nwafu.nexus.domain.response;

import cn.edu.nwafu.nexus.security.dto.UserToken;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 用户登录响应。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class MemberLoginVo extends UserToken {
    private String avatar;
    private String nickname;
    private List<String> roles;

    /**
     * 构造方法。
     *
     * @param userToken 父类 UserToken 对象
     */
    public MemberLoginVo(UserToken userToken) {
        this.setAccessToken(userToken.getAccessToken());
        this.setRefreshToken(userToken.getRefreshToken());
        this.setExpires(userToken.getExpires());
        this.setUsername(userToken.getUsername());
    }
}
