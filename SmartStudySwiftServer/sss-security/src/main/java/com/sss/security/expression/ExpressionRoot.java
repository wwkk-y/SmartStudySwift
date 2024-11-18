package com.sss.security.expression;

import com.sss.security.domain.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("ex-root")
public class ExpressionRoot {
    public static final String ROLE_PREFIX = "ROLE_";

    public boolean hasAuthority(String... authorities){
        // 获取当前用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // 获取filter中设置的权限
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();// 获取权限
        Set<String> permissionsSet = new HashSet<>(permissions);
        for (String authority : authorities) {
            if(permissionsSet.contains(authority)){
                return true;
            }
        }
        return false;
    }

    public boolean hasRole(String... roles){
        // 获取当前用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // 获取filter中设置的权限
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();// 获取权限
        Set<String> permissionsSet = new HashSet<>(permissions);
        for (String role : roles) {
            if(permissionsSet.contains(ROLE_PREFIX + role)){
                return true;
            }
        }
        return false;
    }
}
