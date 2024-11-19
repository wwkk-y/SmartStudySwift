package com.sss.security.service;

import com.sss.security.dao.UmsUserDao;
import com.sss.security.domain.UserDetailsImpl;
import com.sss.security.mapper.UmsUserDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {
    @Resource
    private UmsUserDetailMapper umsUserDetailMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据username获取用户
        UmsUserDao umsUser = umsUserDetailMapper.selectUserByUsername(username);
        if(umsUser == null){
            throw new UsernameNotFoundException("不存在该用户: " + username);
        }

        return new UserDetailsImpl(
                umsUser,
                umsUserDetailMapper.selectPermissionNameByUserId(umsUser.getId()),
                umsUserDetailMapper.selectRoleNameByUserId(umsUser.getId())
        );
    }
}
