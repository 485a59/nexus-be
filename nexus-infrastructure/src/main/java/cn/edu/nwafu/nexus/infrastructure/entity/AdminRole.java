package cn.edu.nwafu.nexus.infrastructure.entity;

import cn.edu.nwafu.nexus.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色信息表。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@TableName("admin_role")
@ApiModel(description = "角色信息表")
public class AdminRole extends BaseEntity<AdminRole> {
    @ApiModelProperty("管理员角色 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("角色名称")
    @TableField("name")
    private String roleName;

    @ApiModelProperty("角色权限字符串")
    @TableField("key")
    private String roleKey;

    @ApiModelProperty("显示顺序")
    @TableField("sort")
    private Integer roleSort;

    @ApiModelProperty("角色状态（1正常 0停用）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;
}
