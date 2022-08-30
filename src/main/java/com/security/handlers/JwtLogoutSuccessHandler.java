package com.security.handlers;

import cn.hutool.json.JSONUtil;
import com.security.common.Result;
import com.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @Author wudengming
 * @Description 登出处理器
 * @Date  2022/8/30
 * @Param
 * @return
**/
@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    JwtUtils jwtUtils;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("退出成功处理器");
        //如果认证的身份凭证不为空，需要手动退出
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        //等处成功将jwt设置为空，因为jwt是无状态的只能在过期后消失
        response.setHeader(jwtUtils.getHeader(),"");
        Result result = Result.success("登出成功",null);

        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

        outputStream.flush();
        outputStream.close();
    }
}
