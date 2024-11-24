package com.sss.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sss.security.config.SecurityConstConfig;
import com.sss.security.dao.UmsUserDao;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class UserDetailsImpl implements UserDetails {
    private final UmsUserDao user;

    private final List<String> permissions; // 权限字符串

    @JsonIgnore // 序列化时忽略属性
    private final List<SimpleGrantedAuthority> authorities;

    public UserDetailsImpl(@NonNull UmsUserDao user, @NonNull List<String> roles, @NonNull List<String> permissions) {
        this.user = user;
        this.permissions = Stream.concat(roles.stream(), permissions.stream()).collect(Collectors.toList()); // 权限
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
        // return user.getStatus() == 1;
        return true;
    }

    public UmsUserDao getUser(){
        return user;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
