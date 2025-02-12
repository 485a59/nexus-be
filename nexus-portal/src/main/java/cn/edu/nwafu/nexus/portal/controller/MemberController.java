package cn.edu.nwafu.nexus.portal.controller;

import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.response.MemberLoginVo;
import cn.edu.nwafu.nexus.domain.service.MemberService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.UploadFileDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.user.MemberLoginDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.user.MemberRegisterDto;
import cn.edu.nwafu.nexus.infrastructure.model.dto.user.UpdateUserPasswordDto;
import cn.edu.nwafu.nexus.infrastructure.model.vo.user.UpdateProfileDto;
import cn.edu.nwafu.nexus.infrastructure.model.vo.user.UserProfileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 用户控制器。
 *
 * @author Huang Z.Y.
 */
@Api(tags = "用户登录 API")
@Tag(name = "用户登录 API", description = "用户登录注册管理")
@RestController
@RequestMapping("/auth")
public class MemberController {
    @Resource
    private MemberService memberService;

    @ApiOperation("用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult<MemberLoginVo> login(@RequestBody MemberLoginDto memberLoginDto) {
        MemberLoginVo memberLoginVo = memberService.login(memberLoginDto.getUsername(), memberLoginDto.getPassword());
        if (memberLoginVo == null) {
            return CommonResult.validate("用户名或密码错误");
        }
        return CommonResult.success(memberLoginVo);
    }

    @Operation(summary = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<?> register(@RequestBody MemberRegisterDto registerDto) {
        memberService.register(registerDto.getUsername(), registerDto.getPassword(),
                registerDto.getTelephone(), registerDto.getAuthCode());
        return CommonResult.success(null, "注册成功");
    }

    @Operation(summary = "获取验证码")
    @RequestMapping(value = "/authCode", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<?> getAuthCode(@RequestParam String telephone) {
        String authCode = memberService.generateAuthCode(telephone);
        return CommonResult.success(authCode, "获取验证码成功");
    }

    @Operation(summary = "获取个人信息")
    @GetMapping
    public CommonResult<UserProfileVo> profile() {
        return CommonResult.success(null);
    }

    @Operation(summary = "修改个人信息")
    @PutMapping
    public CommonResult<Void> updateProfile(@RequestBody UpdateProfileDto command) {
        return CommonResult.success(null);
    }

    @Operation(summary = "重置个人密码")
    @PutMapping("/password")
    public CommonResult<Void> updatePassword(@RequestBody UpdateUserPasswordDto command) {
        return CommonResult.success(null);
    }

    @Operation(summary = "修改个人头像")
    @PostMapping("/avatar")
    public CommonResult<UploadFileDto> avatar(@RequestParam("avatarfile") MultipartFile file) {
        return CommonResult.success(null);
    }
}
