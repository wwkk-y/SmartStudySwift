package com.sss.security.config;

// 常量
public class SecurityConstConfig {
    public static final String TOKEN_REDIS_PREFIX = "TOKEN_"; // token在redis里面的前缀

    /**
     *  用户权限信息redis key前缀
     *  删除条件: 用户名更新、用户角色更新、角色对应权限更新
     */
    public static final String USER_PERMISSION_USERNAME_REDIS_PREFIX = "USER_PERMISSION_";
}
