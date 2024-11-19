package com.sss.ums.controller;

import com.sss.ums.service.UmsAccountService;
import com.sss.ums.vo.AccountRegisterVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private UmsAccountService umsAccountService;

    @GetMapping("/valid")
    public void valid(){
        umsAccountService.registerAccount(null);
    }
}
