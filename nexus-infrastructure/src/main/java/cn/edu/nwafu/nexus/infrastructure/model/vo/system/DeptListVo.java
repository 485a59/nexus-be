package cn.edu.nwafu.nexus.infrastructure.model.vo.system;

import cn.edu.nwafu.nexus.common.annotation.ExcelColumn;
import cn.edu.nwafu.nexus.common.annotation.ExcelSheet;
import lombok.Data;

import java.util.Date;

/**
 * @author Huang Z.Y.
 */
@Data
@ExcelSheet(name = "部门列表")
public class DeptListVo {
    @ExcelColumn(name = "部门ID")
    private Integer id;

    @ExcelColumn(name = "部门名称")
    private String name;

    @ExcelColumn(name = "父部门ID")
    private Long parentId;

    @ExcelColumn(name = "显示顺序")
    private Integer orderNum;

    @ExcelColumn(name = "负责人")
    private String leaderName;

    @ExcelColumn(name = "联系电话")
    private String phone;

    @ExcelColumn(name = "邮箱")
    private String email;

    @ExcelColumn(name = "部门状态")
    private Integer status;

    @ExcelColumn(name = "创建时间")
    private Date createTime;
}
