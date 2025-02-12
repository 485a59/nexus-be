package cn.edu.nwafu.nexus.infrastructure.model.entity;

import cn.edu.nwafu.nexus.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统角色用户关联表。
 *
 * @author Huang Z.Y.
 */
@Data
@TableName("sys_role_user")
@EqualsAndHashCode(callSuper = true)
public class SysRoleUser extends BaseEntity<SysRoleUser> {
    @ApiModelProperty("用户ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户 ID
     */
    @TableField("user_id")
    @ApiModelProperty("用户编号")
    private String userId;

    /**
     * 角色 ID
     */
    @TableField("role_id")
    @ApiModelProperty("角色编号")
    private Integer roleId;
}    