package com.sss.ums.controller;

import com.sss.common.api.RResult;
import com.sss.security.util.AccountUtil;
import com.sss.ums.service.UmsAccountService;
import com.sss.ums.vo.AccountLoginVo;
import com.sss.ums.vo.AccountRegisterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@Api("包含账户管理的所有操作，如登录、注册、下线、注销、修改密码、修改信息。")
@RequestMapping("/ums/account")
public class UmsAccountController {

    @Resource
    private UmsAccountService umsAccountService;
    @Resource
    private AccountUtil accountUtil;

    @PostMapping("/login")
    @ApiOperation("登录 返回token")
    public RResult<String> login(
            @Valid @RequestBody AccountLoginVo account
    ){
        if(!StringUtils.hasText(account.getUsername())
                && !StringUtils.hasText(account.getUsername())){
            return RResult.failed("请输入邮箱或者手机号");
        }

        String token;
        if(account.getUsername() != null) {
            // 用户名登录
            token = umsAccountService.loginByUsername(account.getUsername(), account.getPassword());
        } else {
            // 邮箱登录
            token = umsAccountService.loginByEmail(account.getEmail(), account.getPassword());
        }

        return RResult.success(token);
    }

    @PostMapping("/register")
    @ApiOperation("注册 返回是否注册成功")
    public RResult<Boolean> register(
            @Valid @RequestBody AccountRegisterVo account
    ){
        // 用户名 | 邮箱 不能重复
        return RResult.success(umsAccountService.registerAccount(account));
    }

    @PostMapping("/logout")
    @ApiOperation("当前账号下线")
    public RResult<String> logout(HttpServletRequest request){
        accountUtil.logoutCurAccount(request);
        return RResult.success("");
    }
}
