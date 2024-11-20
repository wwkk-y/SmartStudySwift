package com.sss.security.service;

import com.sss.common.service.RedisService;
import com.sss.security.bo.UserPermissionBo;
import com.sss.security.config.SecurityConstConfig;
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
    @Resource
    private RedisService redisCacheService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据username获取用户 使用缓存
        UserPermissionBo userPermissionBo = (UserPermissionBo) redisCacheService.get(SecurityConstConfig.USER_PERMISSION_USERNAME_REDIS_PREFIX + username);
        if(userPermissionBo == null){
            // 缓存里没有就去缓存里查
            UmsUserDao umsUser = umsUserDetailMapper.selectUserByUsername(username);
            if(umsUser == null){
                throw new UsernameNotFoundException("不存在该用户: " + username);
            }
            userPermissionBo = new UserPermissionBo();
            userPermissionBo.setUmsUserDao(umsUser);
            userPermissionBo.setRoleNames(umsUserDetailMapper.selectRoleNameByUserId(umsUser.getId()));
            userPermissionBo.setPermissionNames(umsUserDetailMapper.selectPermissionNameByUserId(umsUser.getId()));
            redisCacheService.set(SecurityConstConfig.USER_PERMISSION_USERNAME_REDIS_PREFIX + username, userPermissionBo);
        }

        return new UserDetailsImpl(
                userPermissionBo.getUmsUserDao(),
                userPermissionBo.getRoleNames(),
                userPermissionBo.getPermissionNames()
        );

    }
}
