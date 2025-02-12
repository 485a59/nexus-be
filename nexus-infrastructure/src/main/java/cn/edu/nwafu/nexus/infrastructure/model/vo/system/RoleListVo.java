package cn.edu.nwafu.nexus.infrastructure.model.vo.system;

import cn.edu.nwafu.nexus.common.annotation.ExcelColumn;
import cn.edu.nwafu.nexus.common.annotation.ExcelSheet;
import lombok.Data;

import java.util.Date;

/**
 * @author Huang Z.Y.
 */
@Data
@ExcelSheet(name = "角色列表")
public class RoleListVo {
    @ExcelColumn(name = "角色ID")
    private Integer id;
    @ExcelColumn(name = "角色名称")
    private String name;
    @ExcelColumn(name = "角色标识")
    private String key;
    @ExcelColumn(name = "备注")
    private String remark;
    @ExcelColumn(name = "创建时间")
    private Date createTime;
    @ExcelColumn(name = "数据范围")
    private Integer dataScope;
    @ExcelColumn(name = "状态")
    private Integer status;
}
