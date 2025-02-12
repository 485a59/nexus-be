package cn.edu.nwafu.nexus.portal.controller;

import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.service.SysParamService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.param.QueryGroupParamDto;
import cn.edu.nwafu.nexus.infrastructure.model.entity.SysParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Huang Z.Y.
 */
@Api(tags = "系统参数 API")
@Tag(name = "系统参数 API", description = "系统参数管理")
@RestController
@RequestMapping("/param")
public class SysParamController {
    @Resource
    SysParamService sysParamService;

    @Operation(summary = "查询系统参数组", tags = {"系统参数管理"})
    @RequestMapping(value = "/group/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Map<String, Object>> groupList(
            @Parameter(description = "查询参数 DTO", required = false)
            QueryGroupParamDto queryGroupParamDto
    ) {
        List<SysParam> list = sysParamService.list(new QueryWrapper<SysParam>().lambda().eq(SysParam::getGroupName,
                queryGroupParamDto.getGroupName()));
        Map<String, Object> map = new HashMap<>();
        for (SysParam sysParam : list) {
            map.put(sysParam.getKey(), sysParam.getValue());
        }
        return CommonResult.success(map);
    }
}
