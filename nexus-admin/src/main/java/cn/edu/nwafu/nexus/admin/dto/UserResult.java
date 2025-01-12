package cn.edu.nwafu.nexus.admin.dto;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;

import java.util.Date;

/**
 * @author Huang Z.Y.
 */
@Data
public class UserResult {
    private Long userId;
    private Long postId;
    private String postName;
    private Long roleId;
    private String roleName;
    private Long deptId;
    private String deptName;
    private String username;
    private String nickname;
    private Integer userType;
    private String email;
    private String phoneNumber;
    private Integer sex;
    private String avatar;
    private Integer status;
    private String loginIp;
    private Date loginDate;
    private Long creatorId;
    private String creatorName;
    private Date createTime;
    private Long updaterId;
    private String updaterName;
    private Date updateTime;
    private String remark;

    public UserDTO(SysUserEntity entity) {
        if (entity != null) {
            BeanUtil.copyProperties(entity, this);

            SysDeptEntity dept = CacheCenter.deptCache.get(entity.getDeptId() + "");
            if (dept != null) {
                this.deptName = dept.getDeptName();
            }

            SysUserEntity creator = CacheCenter.userCache.getObjectById(entity.getCreatorId());
            if (creator != null) {
                this.creatorName = creator.getUsername();
            }

            if (entity.getRoleId() != null) {
                SysRoleEntity roleEntity = CacheCenter.roleCache.getObjectById(entity.getRoleId());
                this.roleName = roleEntity != null ? roleEntity.getRoleName() : "";
            }

            if (entity.getPostId() != null) {
                SysPostEntity post = CacheCenter.postCache.getObjectById(entity.getRoleId());
                this.postName = post != null ? post.getPostName() : "";
            }

        }
    }

    public UserDTO(SearchUserDO entity) {
        if (entity != null) {
            BeanUtil.copyProperties(entity, this);

            if (entity.getRoleId() != null) {
                SysRoleEntity roleEntity = CacheCenter.roleCache.getObjectById(entity.getRoleId());
                this.roleName = roleEntity != null ? roleEntity.getRoleName() : "";
            }
        }
    }
}
