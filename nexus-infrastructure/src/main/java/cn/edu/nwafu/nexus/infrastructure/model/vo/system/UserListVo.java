package cn.edu.nwafu.nexus.infrastructure.model.vo.system;

import cn.edu.nwafu.nexus.common.annotation.ExcelColumn;
import lombok.Data;

import java.util.Date;

/**
 * @author Huang Z.Y.
 */
@Data
public class UserListVo {
    @ExcelColumn(name = "用户ID")
    private String id;

    @ExcelColumn(name = "角色ID列表")
    private String roleIds;

    @ExcelColumn(name = "角色名称列表")
    private String roleNames;

    @ExcelColumn(name = "部门ID")
    private Integer deptId;

    @ExcelColumn(name = "部门名称")
    private String name;

    @ExcelColumn(name = "用户名")
    private String username;

    @ExcelColumn(name = "用户昵称")
    private String nickname;

    @ExcelColumn(name = "邮件")
    private String email;

    @ExcelColumn(name = "号码")
    private String phoneNumber;

    @ExcelColumn(name = "性别")
    private Integer sex;

    @ExcelColumn(name = "用户头像")
    private String avatar;

    @ExcelColumn(name = "状态")
    private Integer status;

    @ExcelColumn(name = "IP")
    private String loginIp;

    @ExcelColumn(name = "登录时间")
    private Date loginTime;

    @ExcelColumn(name = "创建时间")
    private Date createTime;

    @ExcelColumn(name = "修改时间")
    private Date updateTime;

    @ExcelColumn(name = "备注")
    private String remark;
}
