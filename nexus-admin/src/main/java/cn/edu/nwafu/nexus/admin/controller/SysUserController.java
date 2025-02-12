package cn.edu.nwafu.nexus.admin.controller;

import cn.edu.nwafu.nexus.common.api.CommonPage;
import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.service.SysUserService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.ChangeUserStatusDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.ResetPasswordDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.UpdateUserDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.user.CreateMemberDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.user.UserListDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Member;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.UserListVo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户管理控制器。
 *
 * @author Huang Z.Y.
 */
@Api(tags = "用户管理 API")
@Tag(name = "用户管理 API", description = "用户相关的增删查改")
@RestController
@RequestMapping("/system/user")
@Slf4j
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    @Operation(summary = "用户列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonResult<CommonPage<UserListVo>> list(@RequestBody UserListDto userListDto,
                                                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UserListVo> memberList = sysUserService.list(userListDto, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(memberList));
    }

    @Operation(summary = "用户列表导出")
    @GetMapping("/excel")
    public void exportUserByExcel(HttpServletResponse response, UserListDto userListDto) {
        return;
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public CommonResult<Void> add(@Validated @RequestBody CreateMemberDto command) {
        sysUserService.add(command);
        return CommonResult.success(null);
    }

    @Operation(summary = "修改用户")
    @PutMapping
    public CommonResult<Void> edit(@Validated @RequestBody UpdateUserDto command) {
        return CommonResult.success(null);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{userIds}")
    public CommonResult<Void> remove(@PathVariable List<String> userIds) {
        sysUserService.removeByIds(userIds);
        return CommonResult.success(null);
    }

    @Operation(summary = "重置用户密码")
    @PutMapping("/password")
    public CommonResult<Void> resetPassword(@RequestBody ResetPasswordDto command) {
        sysUserService.resetPassword(command);
        return CommonResult.success(null);
    }

    @Operation(summary = "修改用户状态")
    @PutMapping("/status")
    public CommonResult<Void> changeStatus(@RequestBody ChangeUserStatusDto command) {
        Member member = new Member();
        member.setId(command.getId());
        member.setStatus(command.getStatus());
        sysUserService.updateById(member);
        return CommonResult.success(null);
    }
}
