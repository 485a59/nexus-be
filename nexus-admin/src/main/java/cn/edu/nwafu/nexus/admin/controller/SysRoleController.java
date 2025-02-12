package cn.edu.nwafu.nexus.admin.controller;

import cn.edu.nwafu.nexus.common.api.CommonPage;
import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.service.SysRoleService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.CreateRoleDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.RoleListDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.UpdateRoleDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.UpdateRoleStatusDto;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.RoleListVo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Api(tags = "系统角色 API")
@Tag(name = "系统角色 API", description = "此接口主要用于对系统角色进行全面管理操作")
@RestController
@RequestMapping("/system/role")
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;

    @Operation(summary = "角色列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonResult<CommonPage<RoleListVo>> list(@RequestBody RoleListDto query,
                                                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<RoleListVo> page = sysRoleService.list(query, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "角色列表导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, RoleListDto query) {
        return;
    }

    @Operation(summary = "角色详情")
    @GetMapping(value = "/{roleId}")
    public CommonResult<RoleListDto> getInfo(@PathVariable @NotNull Integer roleId) {
        return CommonResult.success(null);
    }

    @Operation(summary = "添加角色")
    @PostMapping
    public CommonResult<Void> add(@RequestBody CreateRoleDto command) {
        sysRoleService.add(command);
        return CommonResult.success(null);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping(value = "/{roleId}")
    public CommonResult<Void> remove(@PathVariable("roleId") List<Integer> roleIds) {
        sysRoleService.remove(roleIds);
        return CommonResult.success(null);
    }

    @Operation(summary = "修改角色")
    @PutMapping
    public CommonResult<Void> edit(@Validated @RequestBody UpdateRoleDto command) {
        sysRoleService.update(command);
        return CommonResult.success(null);
    }

    @Operation(summary = "修改角色状态")
    @PutMapping("/{roleId}/status")
    public CommonResult<Void> changeStatus(@PathVariable("roleId") Long roleId,
                                           @RequestBody UpdateRoleStatusDto command) {
        sysRoleService.updateStatus(roleId, command);
        return CommonResult.success(null);
    }

    // @Operation(summary = "已关联该角色的用户列表")
    // @PreAuthorize("@permission.has('system:role:list')")
    // @GetMapping("/{roleId}/allocated/list")
    // public CommonResult<CommonPage<UserListVo>> allocatedUserList(@PathVariable("roleId") Integer roleId,
    //                                                             AllocatedRoleDto query) {
    //     Page<UserListVo> page = sysRoleService.getAllocatedList(roleId, query);
    //     return CommonResult.success(CommonPage.restPage(page));
    // }

    // @Operation(summary = "未关联该角色的用户列表")
    // @PreAuthorize("@permission.has('system:role:list')")
    // @GetMapping("/{roleId}/unallocated/list")
    // public CommonResult<CommonPage<UserListVo>> unallocatedUserList(@PathVariable("roleId") Integer roleId,
    //                                                               UnallocatedRoleDto query) {
    //     Page<UserListVo> page = sysRoleService.getUnallocatedList(roleId, query);
    //     return CommonResult.success(CommonPage.restPage(page));
    // }

    @Operation(summary = "批量解除角色和用户的关联")
    @PreAuthorize("@permission.has('system:role:edit')")
    @DeleteMapping("/users/{userIds}/grant/bulk")
    public CommonResult<Void> deleteRoleOfUserByBulk(@PathVariable("userIds") List<String> userIds) {
        // sysRoleService.cancelAuthUser(userIds);
        return CommonResult.success(null);
    }

    @Operation(summary = "批量添加用户和角色关联")
    @PreAuthorize("@permission.has('system:role:edit')")
    @PostMapping("/{roleId}/users/{userIds}/grant/bulk")
    public CommonResult<Void> addRoleForUserByBulk(@PathVariable("roleId") Integer roleId,
                                                   @PathVariable("userIds") List<String> userIds) {
        // sysRoleService.authUsers(roleId, userIds);
        return CommonResult.success(null);
    }
}
