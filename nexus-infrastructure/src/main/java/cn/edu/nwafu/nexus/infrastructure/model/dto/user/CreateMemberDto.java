package cn.edu.nwafu.nexus.infrastructure.model.dto.user;

import cn.edu.nwafu.nexus.common.annotation.ExcelColumn;
import lombok.Data;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Data
public class CreateMemberDto {
    @ExcelColumn(name = "部门ID")
    private Long deptId;

    @ExcelColumn(name = "用户名")
    private String username;

    @ExcelColumn(name = "昵称")
    private String nickname;

    @ExcelColumn(name = "邮件")
    private String email;

    @ExcelColumn(name = "电话号码")
    private String phoneNumber;

    @ExcelColumn(name = "性别")
    private Integer sex;

    @ExcelColumn(name = "头像")
    private String avatar;

    @ExcelColumn(name = "密码")
    private String password;

    @ExcelColumn(name = "状态")
    private Integer status;

    @ExcelColumn(name = "角色ID列表")
    private List<Integer> roleIds;

    @ExcelColumn(name = "备注")
    private String remark;
}
