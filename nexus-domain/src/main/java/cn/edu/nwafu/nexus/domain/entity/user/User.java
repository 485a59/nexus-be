package cn.edu.nwafu.nexus.domain.entity.user;

import cn.edu.nwafu.nexus.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

/**
 * 用户信息表。
 *
 * @author Huang Z.Y.
 */
@Getter
@Setter
@TableName("user")
@ApiModel(description = "用户信息表")
public class User extends BaseEntity<User> {
    @ApiModelProperty("用户 ID")
    @Id
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

    @ApiModelProperty("所在院系")
    @TableField("department")
    private String department;

    @ApiModelProperty("帐号状态（1 正常 2 停用 3 冻结）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("最后登录IP")
    @TableField("login_ip")
    private String loginIp;

    @ApiModelProperty("最后登录时间")
    @TableField("login_date")
    private Date loginDate;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;
}
