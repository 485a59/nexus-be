package cn.edu.nwafu.nexus.admin.controller;

import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.service.SysDeptService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.CreateDeptDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.DeptListDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.system.UpdateDeptDto;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.DeptDetailVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.system.DeptListVo;
import cn.hutool.core.lang.tree.Tree;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
@RequestMapping("/system/dept")
@Api(tags = "系统部门 API")
@Tag(name = "系统部门 API", description = "此接口主要用于对系统部门进行管理操作，涵盖部门信息的创建、查询、更新以及删除等功能")
@RestController
@Slf4j
public class SysDeptController {
    @Resource
    private SysDeptService sysDeptService;

    @Operation(summary = "部门列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonResult<List<DeptListVo>> list(DeptListDto query) {
        List<DeptListVo> list = sysDeptService.list(query);
        return CommonResult.success(list);
    }

    @Operation(summary = "部门详情")
    @GetMapping(value = "/{deptId}")
    public CommonResult<DeptDetailVo> getInfo(@PathVariable @NotNull Long deptId) {
        DeptDetailVo info = sysDeptService.getInfo(deptId);
        return CommonResult.success(info);
    }

    @Operation(summary = "获取部门树级结构")
    @GetMapping("/dropdown")
    public CommonResult<List<Tree<Integer>>> dropdownList() {
        List<Tree<Integer>> trees = sysDeptService.buildDeptTreeSelect();
        return CommonResult.success(trees);
    }

    @Operation(summary = "新增部门")
    @PostMapping
    public CommonResult<Void> add(@Validated @RequestBody CreateDeptDto command) {
        sysDeptService.add(command);
        return CommonResult.success(null);
    }

    @Operation(summary = "修改部门")
    @PutMapping("/{deptId}")
    public CommonResult<Void> edit(@PathVariable("deptId") Long deptId,
                                   @Validated @RequestBody UpdateDeptDto command) {
        sysDeptService.update(deptId, command);
        return CommonResult.success(null);
    }

    @Operation(summary = "删除部门")
    @DeleteMapping("/{deptId}")
    public CommonResult<Void> remove(@PathVariable @NotNull Long deptId) {
        sysDeptService.remove(deptId);
        return CommonResult.success(null);
    }
}