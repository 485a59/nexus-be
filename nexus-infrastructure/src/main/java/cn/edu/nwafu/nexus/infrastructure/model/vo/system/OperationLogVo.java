package cn.edu.nwafu.nexus.infrastructure.model.vo.system;

import cn.edu.nwafu.nexus.common.annotation.ExcelColumn;
import cn.edu.nwafu.nexus.common.annotation.ExcelSheet;
import lombok.Data;

import java.util.Date;

/**
 * @author Huang Z.Y.
 */
@Data
@ExcelSheet(name = "操作日志")
public class OperationLogVo {
    @ExcelColumn(name = "ID")
    private Long operationId;

    private Integer businessType;

    @ExcelColumn(name = "操作类型")
    private String businessTypeStr;

    @ExcelColumn(name = "操作类型")
    private String requestMethod;

    @ExcelColumn(name = "操作类型")
    private String requestModule;

    @ExcelColumn(name = "操作类型")
    private String requestUrl;

    @ExcelColumn(name = "操作类型")
    private String calledMethod;

    private Integer operatorType;

    @ExcelColumn(name = "操作人类型")
    private String operatorTypeStr;

    @ExcelColumn(name = "用户ID")
    private Long userId;

    @ExcelColumn(name = "用户名")
    private String username;

    @ExcelColumn(name = "ip地址")
    private String operatorIp;

    @ExcelColumn(name = "ip地点")
    private String operatorLocation;

    @ExcelColumn(name = "部门ID")
    private Long deptId;

    @ExcelColumn(name = "部门")
    private String name;

    @ExcelColumn(name = "操作参数")
    private String operationParam;

    @ExcelColumn(name = "操作结果")
    private String operationResult;

    private Integer status;

    @ExcelColumn(name = "状态")
    private String statusStr;

    @ExcelColumn(name = "错误堆栈")
    private String errorStack;

    @ExcelColumn(name = "操作时间")
    private Date operationTime;
}
