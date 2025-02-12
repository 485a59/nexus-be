package cn.edu.nwafu.nexus.infrastructure.model.entity;

import cn.edu.nwafu.nexus.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 角色信息表。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@TableName("sys_role")
@ApiModel(description = "角色信息表")
public class SysRole extends BaseEntity<SysRole> {
    @ApiModelProperty("角色编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("角色名称")
    @TableField("role_name")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    private String roleName;

    @ApiModelProperty("角色标识")
    @TableField("role_key")
    @NotBlank(message = "角色标识不能为空")
    @Size(max = 50, message = "角色标识长度不能超过50个字符")
    private String roleKey;

    @ApiModelProperty("备注")
    @TableField("remark")
    @Size(max = 200, message = "备注长度不能超过200个字符")
    private String remark;

    @ApiModelProperty("角色状态（0：禁用，1：启用）")
    @TableField("status")
    private Integer status;
}