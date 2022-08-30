package com.security.handlers;

import cn.hutool.json.JSONUtil;

import com.security.common.Result;
import com.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @Author wudengming
 * @Description 登陆成功处理器
 * @Date  2022/8/30
 * @Param
 * @return
**/
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler{

    @Autowired
    JwtUtils jwtUtils;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        String username = authentication.getName();
        // 生成jwt，并放置到请求头中
        String jwt = jwtUtils.generateToken(username);
        //将jwt放入response header中：Authorization
        response.setHeader(jwtUtils.getHeader(), jwt);

        Result result = Result.success("登陆成功过滤器执行",null);

        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
