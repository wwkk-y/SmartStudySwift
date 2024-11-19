package com.sss.security.util;

import com.sss.common.service.RedisService;
import com.sss.security.config.SecurityConstConfig;
import com.sss.security.dao.UmsUserDao;
import com.sss.security.domain.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 账户工具类
 */
public class AccountUtil {
    @Resource
    private JWTUtil jwtUtil;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenKey}")
    private String tokenKey;
    @Resource
    private RedisService redisService;

    /**
     * 返回当前登录的用户
     * @return 未登录返回 null
     */
    public UmsUserDao getCurUser(){
        UserDetailsImpl loginUser = null;
        try{
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            loginUser = (UserDetailsImpl) authentication.getPrincipal();
        } catch (RuntimeException e){
            return null;
        }

        return loginUser.getUser();
    }

    /**
     * 返回当前登录用户的token
     * @return token
     */
    private String getCurToken(HttpServletRequest request){
        return request.getHeader(tokenKey);
    }

    /**
     * 登录用户并返回token
     * @return token
     */
    public String loginAccount(String username) {
        String token = jwtUtil.generateTokenFromUsername(username);
        redisService.set(SecurityConstConfig.TOKEN_REDIS_PREFIX + token, true, expiration);
        return token;
    }

    /**
     * 下线用户
     */
    public void logoutCurAccount(HttpServletRequest request){
        String curToken = getCurToken(request);
        if (curToken != null){
            redisService.del(SecurityConstConfig.TOKEN_REDIS_PREFIX + curToken);
        }
    }
}
