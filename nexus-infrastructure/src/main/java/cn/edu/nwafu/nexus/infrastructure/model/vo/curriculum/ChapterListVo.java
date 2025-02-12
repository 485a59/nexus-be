package cn.edu.nwafu.nexus.infrastructure.model.vo.curriculum;

import cn.edu.nwafu.nexus.common.annotation.ExcelColumn;
import cn.edu.nwafu.nexus.common.annotation.ExcelSheet;
import lombok.Data;

import java.util.Date;

/**
 * @author Huang Z.Y.
 */
@Data
@ExcelSheet(name = "章节列表")
public class ChapterListVo {
    @ExcelColumn(name = "章节ID")
    private Integer id;

    @ExcelColumn(name = "章节名称")
    private String name;

    @ExcelColumn(name = "父部门ID")
    private Long parentId;

    @ExcelColumn(name = "章节状态")
    private Integer status;
    
    @ExcelColumn
    private Integer orderNum;

    @ExcelColumn(name = "创建时间")
    private Date createTime;
}
