package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.dto.system.CreateRoleDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.RoleListDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.UpdateRoleDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.UpdateRoleStatusDto;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.RoleListVo;

import java.util.List;

public interface SysRoleService {
    List<RoleListVo> list(RoleListDto query, Integer pageSize, Integer pageNum);

    void add(CreateRoleDto command);

    void update(UpdateRoleDto command);

    void remove(List<Integer> roleIds);

    void updateStatus(Long roleId, UpdateRoleStatusDto command);

    // void cancelAuthUser(List<String> userIds);

    // void authUsers(Integer roleId, List<String> userIds);
} 