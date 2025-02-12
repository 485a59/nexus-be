package cn.edu.nwafu.nexus.infrastructure.mapper;

import cn.edu.nwafu.nexus.infrastructure.model.dto.system.RoleListDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.SysRole;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.RoleListVo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    List<RoleListVo> selectList(RoleListDto query);
}
