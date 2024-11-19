package com.sss.ums.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AccountLoginVo {
    private String username;

    private String email;

    @NotEmpty(message = "密码不能为空")
    private String password;
}
