package com.security.filter;

import com.security.handlers.LoginFailureHandler;
import com.security.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*CaptchaFilter用于验证码验证
* 因为验证码值需要一次校验，所以继承OncePerRequestFilter
* 自己写的过滤器要在security配置类中配置
* */
@Slf4j
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    LoginFailureHandler loginFailureHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("开始验证码验证");
        String url = request.getRequestURI();
        //只有登陆时才会验证验证码
        if("/login".equals(url) && request.getMethod().equals("POST")){
//            try{
//                //校验验证码
//                validate(request);
//            }catch (CaptchaException e){
                //发现异常，则交给登录失败处理器
//                loginFailureHandler.onAuthenticationFailure(request,response,e);
//            }
        }
        //将请求转发给下一个过滤器
        filterChain.doFilter(request,response);
    }
//    //校验验证码逻辑
//    private void validate(HttpServletRequest request) {
//        String key = request.getParameter("tokens");
//        String code = request.getParameter("code");
//        if(StringUtils.isEmpty(key) || StringUtils.isEmpty(code) ){
//            throw new RuntimeException("验证码信息为空");
//        }
//        if(!code.equals(redisUtil.hget(Const.CAPTCHA_KEY,key))){
//            throw new RuntimeException("验证码错误");
//        }
//        //保证每个验证码只使用一次:安全
//        redisUtil.del(Const.CAPTCHA_KEY,key);
//    }
}
