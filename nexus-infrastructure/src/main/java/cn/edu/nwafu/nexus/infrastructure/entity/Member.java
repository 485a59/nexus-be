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

import java.util.Date;

/**
 * 用户信息表。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@TableName("member")
@ApiModel(description = "用户信息表")
public class Member extends BaseEntity<Member> {
    @ApiModelProperty("用户 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("角色 ID")
    @TableField("role_id")
    private Long roleId;

    @ApiModelProperty("用户账号")
    @TableField("username")
    private String username;

    @ApiModelProperty("用户昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty("用户邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("手机号码")
    @TableField("phone_number")
    private String phoneNumber;

    @ApiModelProperty("用户性别（0男 1女 2未知）")
    @TableField("sex")
    private Integer sex;

    @ApiModelProperty("头像地址")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty("密码")
    @TableField("password")
    private String password;

    @ApiModelProperty("帐号状态（1 正常 2 停用 3 冻结）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("用户角色（例如：[ROLE_STUDENT, ROLE_TEACHER]）")
    @TableField("roles")
    private String roles;

    @ApiModelProperty("用户权限（例如：[permission:btn:delete, permission:btn:edit]）")
    @TableField("permissions")
    private String permissions;

    @ApiModelProperty("最后登录IP")
    @TableField("login_ip")
    private String loginIp;

    @ApiModelProperty("最后登录时间")
    @TableField("login_date")
    private Date loginDate;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

//    /**
//     * 将 roles 字段从 String 转换为 Set<String>。
//     */
//    public Set<String> getRoles() {
//        if (this.roles == null || this.roles.isEmpty()) {
//            return new HashSet<>();
//        }
//        return new HashSet<>(Arrays.asList(this.roles.split(",")));
//    }
//
//    /**
//     * 将 roles 字段从 Set<String> 转换为 String。
//     */
//    public void setRoles(Set<String> roles) {
//        this.roles = String.join(",", roles);
//    }
//
//    /**
//     * 将 permissions 字段从 String 转换为 Set<String>。
//     */
//    public Set<String> getPermissions() {
//        if (this.permissions == null || this.permissions.isEmpty()) {
//            return new HashSet<>();
//        }
//        return new HashSet<>(Arrays.asList(this.permissions.split(",")));
//    }
//
//    /**
//     * 将 permissions 字段从 Set<String> 转换为 String。
//     */
//    public void setPermissions(Set<String> permissions) {
//        this.permissions = String.join(",", permissions);
//    }
}
