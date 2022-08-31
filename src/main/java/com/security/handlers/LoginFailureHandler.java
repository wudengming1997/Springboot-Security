package com.security.handlers;

import cn.hutool.json.JSONUtil;
import com.security.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @Author wudengming
 * @Description 登陆失败处理器
 * @Date  2022/8/30
 * @Param
 * @return
**/
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        //security的返回值是重定向，对于前后端分离的项目需要返回json数据，所以使用流的形式
        //因为返回类型是void，并且返回值是json类型数据，所以要使用到流
        ServletOutputStream outputStream = response.getOutputStream();
        Result result = Result.fail(exception.getMessage());
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

        outputStream.flush();
        outputStream.close();
    }
}
