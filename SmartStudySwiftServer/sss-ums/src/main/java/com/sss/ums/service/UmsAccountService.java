package com.sss.ums.service;

import com.sss.common.api.RException;
import com.sss.common.dao.UmsUser;
import com.sss.security.util.AccountUtil;
import com.sss.ums.mapper.UmsAccountMapper;
import com.sss.ums.vo.AccountRegisterVo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;

@Service
@Slf4j
@Validated
public class UmsAccountService {

    @Resource
    private AccountUtil accountUtil;
    @Resource
    private UmsAccountMapper umsAccountMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    public String loginByUsername(@NonNull String username, @NonNull String password){
        UmsUser account = umsAccountMapper.getUserAccountByUsername(username);
        if(account == null){
            throw new RException("用户不存在");
        }
        if(!passwordEncoder.matches(password, account.getPassword())){
            throw new RException("密码错误");
        }

        return accountUtil.loginAccount(username);
    }

    public String loginByEmail(@NonNull String email, @NonNull String password){
        UmsUser account = umsAccountMapper.getUserAccountByEmail(email);
        if(account == null){
            throw new RException("用户不存在");
        }
        if(!passwordEncoder.matches(password, account.getPassword())){
            throw new RException("密码错误");
        }

        return accountUtil.loginAccount(account.getUsername());
    }

    public boolean registerAccount(@NonNull @Valid AccountRegisterVo account) {
        // 邮箱 | 用户名 不能重复
        if(umsAccountMapper.existsAccount(account.getUsername(), account.getEmail())){
            return false;
        }
        // 加密密码
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        // 设置默认值
        account.setDefaultValue();
        // 注册
        umsAccountMapper.registerAccount(account);
        return true;
    }
}
