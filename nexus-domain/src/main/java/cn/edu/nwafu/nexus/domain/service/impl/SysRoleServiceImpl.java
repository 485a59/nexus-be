package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.domain.service.SysRoleService;
import cn.edu.nwafu.nexus.infrastructure.mapper.SysRoleMapper;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.CreateRoleDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.RoleListDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.UpdateRoleDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.UpdateRoleStatusDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.SysRole;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.RoleListVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Resource
    private SysRoleMapper roleMapper;

    @Override
    public List<RoleListVo> list(RoleListDto query, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return roleMapper.selectList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(CreateRoleDto command) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(command, role);
        save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UpdateRoleDto command) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(command, role);
        updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(List<Integer> roleIds) {
        removeByIds(roleIds);
    }

    @Override
    public void updateStatus(Long roleId, UpdateRoleStatusDto command) {
        SysRole role = new SysRole();
        role.setId(roleId.intValue());
        role.setStatus(command.getStatus());
        updateById(role);
    }

    // @Override
    // @Transactional(rollbackFor = Exception.class)
    // public void cancelAuthUser(List<String> userIds) {
    //     roleMapper.deleteUserRoleRelations(userIds);
    // }

    // @Override
    // @Transactional(rollbackFor = Exception.class)
    // public void authUsers(Integer roleId, List<String> userIds) {
    //     roleMapper.insertUserRoleRelations(roleId, userIds);
    // }
} 