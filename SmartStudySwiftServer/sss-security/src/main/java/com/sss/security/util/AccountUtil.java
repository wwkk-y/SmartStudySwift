package com.sss.security.util;

import com.sss.common.dao.UmsLoginLog;
import com.sss.common.service.RedisService;
import com.sss.security.config.SecurityConstConfig;
import com.sss.security.dao.UmsUserDao;
import com.sss.security.domain.UserDetailsImpl;
import com.sss.security.mapper.UmsUserDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

/**
 * 账户工具类
 */
@Slf4j
public class AccountUtil {
    @Resource
    private JWTUtil jwtUtil;
    @Value("${login.expiration}")
    private Long expiration;
    @Resource
    private RedisService redisService;
    @Resource
    private UmsUserDetailMapper umsUserDetailMapper;

    /**
     * 返回当前登录的用户
     * @return 未登录返回 null
     */
    public UmsUserDao getCurAccount(){
        UserDetailsImpl loginUser;
        try{
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            loginUser = (UserDetailsImpl) authentication.getPrincipal();
        } catch (RuntimeException e){
            return null;
        }

        return loginUser.getUser();
    }

    /**
     * 登录用户并返回token
     * @return token
     */
    public String loginAccount(String username) {
        // 设置登录时间
        umsUserDetailMapper.flushAccountLoginTime(username);
        // 保存token
        String token = jwtUtil.generateToken(username);
        redisService.set(SecurityConstConfig.TOKEN_REDIS_PREFIX + token, true, expiration);
        // 新增登录日志
        umsUserDetailMapper.addLoginLog(username, token);
        return token;
    }

    /**
     * 下线用户
     */
    public void logoutCurAccount(HttpServletRequest request){
        String curToken = jwtUtil.getCurToken(request);
        UmsUserDao curAccount = getCurAccount();
        if (curToken != null && curAccount != null){
            redisService.del(SecurityConstConfig.TOKEN_REDIS_PREFIX + curToken);
            // 设置登录日志状态 -> 下线
            umsUserDetailMapper.loginLogLogout(curAccount.getUsername());
        }
    }

    /**
     * 判断用户登录是否过期
     * @param username 用户名
     * @param token 保存在 token 中的header
     * @return 未过期 -> true
     */
    public boolean notExpire(String username, String token) {
        // 判断登录是否过期, redisCache -> loginTime
        boolean ans = false;
        try{ // 在每一次请求都会执行，不能依靠redis的可靠性
            ans = redisService.hasKey(SecurityConstConfig.TOKEN_REDIS_PREFIX + token);
        } catch (Exception e){
            e.printStackTrace();
            log.error("redis is unusable! use mysql to query login_log {username = {}, token = {}}", username, token);
            // 查询数据库里的登录日志根据登录时间和是否下线看登录过期没有
            UmsLoginLog loginLog = umsUserDetailMapper.getLoginLog(username, token);
            if(loginLog != null && loginLog.getLogout() == 0){
                // 登录时间 > 当前时间 - 过期时间
                Timestamp minLoginTime = new Timestamp(System.currentTimeMillis() - 1000 * expiration);
                if(loginLog.getLoginTime() != null && loginLog.getLoginTime().after(minLoginTime)){
                    ans = true;
                }
            }
        }

        return ans;
    }
}
