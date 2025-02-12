package cn.edu.nwafu.nexus.admin.controller;

import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.response.MemberLoginVo;
import cn.edu.nwafu.nexus.domain.service.MemberService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.user.MemberLoginDto;
import cn.edu.nwafu.nexus.infrastructure.model.vo.user.UserProfileVo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static cn.edu.nwafu.nexus.common.constant.Constants.ADMIN_KEY;
import static cn.edu.nwafu.nexus.common.constant.Constants.SUPER_ADMIN_KEY;

/**
 * 系统用户登录控制器
 *
 * @author Huang Z.Y.
 */
@Api(tags = "系统登录 API")
@Tag(name = "系统登录 API", description = "系统用户登录管理")
@RestController
@RequestMapping("/system")
public class SysLoginController {
    @Resource
    private MemberService memberService;

    @Operation(summary = "系统用户登录")
    @PostMapping("/login")
    public CommonResult<MemberLoginVo> login(@RequestBody MemberLoginDto loginDto) {
        MemberLoginVo loginVo = memberService.login(loginDto.getUsername(), loginDto.getPassword());
        if (loginVo == null) {
            return CommonResult.validate("用户名或密码错误");
        }
        // 验证是否具有系统用户角色
        if (!loginVo.getRoles().contains(ADMIN_KEY)
                && !loginVo.getRoles().contains(SUPER_ADMIN_KEY)) {
            return CommonResult.forbidden(null);
        }
        return CommonResult.success(loginVo);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/profile")
    public CommonResult<UserProfileVo> getProfile() {
        UserProfileVo profile = memberService.getCurrentUserProfile();
        return CommonResult.success(profile);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public CommonResult<Void> logout() {
        memberService.logout();
        return CommonResult.success(null);
    }
}
