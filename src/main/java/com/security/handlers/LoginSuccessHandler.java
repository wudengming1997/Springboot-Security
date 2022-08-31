package com.security.handlers;

import cn.hutool.json.JSONUtil;

import com.security.common.Result;
import com.security.utils.JwtUtils;
import com.security.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

/**
 * @Author wudengming
 * @Description 登陆成功处理器
 * @Date 2022/8/30
 * @Param
 * @return
 **/
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        Result result = Result.success("登陆成功过滤器执行", jwtUtils.generateToken(authentication.getName()));
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
