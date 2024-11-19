package com.sss.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sss.common.dao.UmsUser;
import com.sss.security.config.SecurityLogicConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserDetailsImpl implements UserDetails {
    private final UmsUser user;

    private final List<String> permissions; // 权限字符串

    @JsonIgnore // 序列化时忽略属性
    private final List<SimpleGrantedAuthority> authorities;

    public UserDetailsImpl(UmsUser user, List<String> permissions, List<String> roles) {
        this.user = user;
        Stream<String> roleStream = roles.stream().map(s -> SecurityLogicConfig.ROLE_PREFIX + s); // 角色
        this.permissions = Stream.concat(roleStream, permissions.stream()).collect(Collectors.toList()); // 权限
        // 权限 = 角色 + 角色权限
        authorities = this.permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == 1;
    }

    public UmsUser getUser(){
        return user;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}