package cn.edu.nwafu.nexus.infrastructure.model.vo.system;

import cn.edu.nwafu.nexus.common.annotation.ExcelColumn;
import cn.edu.nwafu.nexus.common.annotation.ExcelSheet;
import lombok.Data;

import java.util.Date;

/**
 * @author Huang Z.Y.
 */
@Data
@ExcelSheet(name = "登录日志")
public class LoginLogVo {
    @ExcelColumn(name = "ID")
    private String logId;

    @ExcelColumn(name = "用户名")
    private String username;

    @ExcelColumn(name = "ip地址")
    private String ipAddress;

    @ExcelColumn(name = "登录地点")
    private String loginLocation;

    @ExcelColumn(name = "操作系统")
    private String operationSystem;

    @ExcelColumn(name = "浏览器")
    private String browser;

    private Integer status;

    @ExcelColumn(name = "状态")
    private String statusStr;

    @ExcelColumn(name = "描述")
    private String msg;

    @ExcelColumn(name = "登录时间")
    private Date loginTime;

}
