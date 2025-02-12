package cn.edu.nwafu.nexus.infrastructure.mapper;

import cn.edu.nwafu.nexus.infrastructure.model.dto.user.UserListDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Member;
import cn.edu.nwafu.nexus.infrastructure.model.entity.SysRole;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.UserListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据访问层。
 *
 * @author Huang Z.Y.
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {
    List<UserListVo> selectUserWithDept(@Param("param") UserListDto param);

    List<SysRole> getRolesByUserId(@Param("userId") String userId);
}
