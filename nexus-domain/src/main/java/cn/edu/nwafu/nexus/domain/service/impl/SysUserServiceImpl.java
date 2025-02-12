package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.common.exception.ApiException;
import cn.edu.nwafu.nexus.domain.service.*;
import cn.edu.nwafu.nexus.domain.util.SystemFileUtils;
import cn.edu.nwafu.nexus.infrastructure.mapper.MemberMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.SysRoleUserMapper;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.ResetPasswordDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.user.CreateMemberDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.user.UserListDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Member;
import cn.edu.nwafu.nexus.infrastructure.model.entity.SysRoleUser;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.UserListVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<MemberMapper, Member> implements SysUserService {
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private UserFileService userFileService;
    @Resource
    private StorageService storageService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private SysRoleUserMapper roleUserMapper;
    @Resource
    private MemberCacheService memberCacheService;
    @Resource
    private FileService fileService;

    @Override
    public List<UserListVo> list(UserListDto param, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return memberMapper.selectUserWithDept(param);
    }

    @Override
    public void add(CreateMemberDto createMemberDto) {
        Member member = new Member();
        BeanUtils.copyProperties(createMemberDto, member);
        member.setStatus(0);
        member.setLoginTime(new Date());
        if (StringUtils.hasText(member.getPassword())) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        memberMapper.insert(member);
        // 设置用户角色关系
        for (Integer roleId : createMemberDto.getRoleIds()) {
            SysRoleUser roleUser = new SysRoleUser();
            roleUser.setUserId(member.getId());
            roleUser.setRoleId(roleId);
            roleUserMapper.insert(roleUser);
        }
        // 添加用户的默认目录
        SystemFileUtils.getDir(member.getId(), "/", "");

    }

    @Override
    public void resetPassword(ResetPasswordDto command) {
        // 验证用户是否存在
        Member member = memberMapper.selectById(command.getId());
        if (member == null) {
            throw new ApiException("用户不存在");
        }
        // 更新密码
        Member updateMember = new Member();
        updateMember.setId(command.getId());
        updateMember.setPassword(passwordEncoder.encode(command.getPassword()));
        memberMapper.updateById(updateMember);
        memberCacheService.delMember(command.getId());
    }
}