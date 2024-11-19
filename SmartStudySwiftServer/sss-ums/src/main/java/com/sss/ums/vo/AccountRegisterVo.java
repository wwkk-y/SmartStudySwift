package com.sss.ums.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

// 账户
@Data
public class AccountRegisterVo {
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

    private String icon;

    @NotEmpty(message = "邮箱地址不能为空")
    private String email;

    @NotEmpty(message = "昵称不能为空")
    private String nickName;

    private String note;

    public void setDefaultValue(){
        if(icon == null){
            icon = "default.png";
        }
        if(note == null){
            note = "";
        }
    }
}
