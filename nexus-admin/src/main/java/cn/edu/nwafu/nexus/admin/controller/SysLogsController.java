package cn.edu.nwafu.nexus.admin.controller;

import cn.edu.nwafu.nexus.common.api.CommonPage;
import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.LoginLogDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.OperationLogDto;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.LoginLogVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.OperationLogVo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Api(tags = "系统日志 API")
@Tag(name = "系统日志 API", description = "此接口主要用于对系统日志进行全面管理操作")
@RestController
@RequestMapping("/system/logs")
public class SysLogsController {
    @Operation(summary = "登录日志列表")
    @GetMapping("/loginLogs")
    public CommonResult<CommonPage<LoginLogVo>> loginInfoList(LoginLogDto loginLogDto) {
        return CommonResult.success(null);
    }

    @Operation(summary = "登录日志导出", description = "将登录日志导出到excel")
    @GetMapping("/loginLogs/excel")
    public void loginInfosExcel(HttpServletResponse response, LoginLogDto query) {
    }

    @Operation(summary = "删除登录日志")
    @DeleteMapping("/loginLogs")
    public CommonResult<Void> removeLoginInfos(@RequestParam @NotNull @NotEmpty List<Long> ids) {
        return CommonResult.success(null);
    }

    @Operation(summary = "操作日志列表")
    @GetMapping("/operationLogs")
    public CommonResult<CommonPage<OperationLogVo>> operationLogs(OperationLogDto query) {
        return CommonResult.success(null);
    }

    @Operation(summary = "操作日志导出")
    @GetMapping("/operationLogs/excel")
    public void operationLogsExcel(HttpServletResponse response, OperationLogDto query) {
        return;
    }

    @Operation(summary = "删除操作日志")
    @DeleteMapping("/operationLogs")
    public CommonResult<Void> removeOperationLogs(@RequestParam List<Long> operationIds) {
        return CommonResult.success(null);
    }
}
