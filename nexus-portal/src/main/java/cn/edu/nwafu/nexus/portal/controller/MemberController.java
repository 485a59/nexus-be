package cn.edu.nwafu.nexus.portal.controller;

import cn.edu.nwafu.nexus.common.api.CommonResult;
import cn.edu.nwafu.nexus.domain.request.MemberLoginDto;
import cn.edu.nwafu.nexus.domain.request.MemberRegisterDto;
import cn.edu.nwafu.nexus.domain.response.MemberLoginVo;
import cn.edu.nwafu.nexus.domain.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户控制器。
 *
 * @author Huang Z.Y.
 */
@Api(tags = "MemberController")
@Tag(name = "MemberController", description = "用户登录注册管理")
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

    @ApiOperation("用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<?> register(@RequestBody MemberRegisterDto registerDto) {
        memberService.register(registerDto.getUsername(), registerDto.getPassword(),
                registerDto.getTelephone(), registerDto.getAuthCode());
        return CommonResult.success(null, "注册成功");
    }

    @ApiOperation("获取验证码")
    @RequestMapping(value = "/authCode", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<?> getAuthCode(@RequestParam String telephone) {
        String authCode = memberService.generateAuthCode(telephone);
        return CommonResult.success(authCode, "获取验证码成功");
    }
}
