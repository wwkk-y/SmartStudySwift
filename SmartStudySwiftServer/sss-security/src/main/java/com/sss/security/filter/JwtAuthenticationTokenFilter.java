package com.sss.security.filter;

import com.sss.common.api.RCode;
import com.sss.common.api.RException;
import com.sss.common.service.RedisService;
import com.sss.security.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT登录授权过滤器
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTUtil jwtUtil;
    @Value("${jwt.tokenKey}")
    private String tokenKey;
    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;
    @Resource
    private RedisService redisService;
    private static final String TOKEN_REDIS_PREFIX = "token_";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(tokenKey);

        if (authHeader != null && authHeader.startsWith(tokenPrefix)) {
            // 去掉前缀，解析 token -> username
            String authToken = authHeader.substring(tokenPrefix.length());// The part after "Bearer "
            String username = jwtUtil.getUserNameFromToken(authToken);
            LOGGER.info("checking username: {}", username);

            if (redisService.hasKey(TOKEN_REDIS_PREFIX + authHeader) && // 看过期没有
                    username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 放置用户信息及其权限信息到上下文

                // 根据用户名获取用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 增强认证过程的安全性
                    LOGGER.info("authenticated user: {}", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
