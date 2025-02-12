package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.dto.system.ResetPasswordDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.user.CreateMemberDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.user.UserListDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Member;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.UserListVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
public interface SysUserService extends IService<Member> {
    List<UserListVo> list(UserListDto param, Integer pageSize, Integer pageNum);

    void add(CreateMemberDto member);

    /**
     * 重置用户密码
     *
     * @param command 重置密码命令
     */
    void resetPassword(ResetPasswordDto command);
}
