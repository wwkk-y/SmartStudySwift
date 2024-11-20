package com.sss.security.filter;

import com.sss.security.util.AccountUtil;
import com.sss.security.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
    @Resource
    private AccountUtil accountUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String token = jwtUtil.getCurToken(request);

        if (token != null) {
            // 去掉前缀，解析 token -> username
            String username = jwtUtil.getUserNameFromToken(token);
            LOGGER.info("checking username: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null
                    && accountUtil.notExpire(username, token) // 登录没有过期时
            ) {
                // 放置用户信息及其权限信息到上下文
                // 根据用户名获取用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( // 上下文用户信息
                            userDetails, // 用户信息
                            null,
                            userDetails.getAuthorities() // 权限
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
