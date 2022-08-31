package com.security.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.security.service.iml.UserService;
import com.security.utils.JwtUtils;
import com.security.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//jwt认证过滤器
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;
    @Autowired
    RedisUtil redisUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(jwtUtils.getHeader());
        if (jwt != null) {
            Claims claim = jwtUtils.getClaimByToken(jwt);
            if (claim != null) {
                if (jwtUtils.isTokenExpired(claim)){
                    String username = claim.getSubject();
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails user = userService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
                        auth.setDetails(user);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }else {

                }
            }
        } else {
            log.info("首次登陆 jwt为空");
        }
        chain.doFilter(request, response);
    }
}
