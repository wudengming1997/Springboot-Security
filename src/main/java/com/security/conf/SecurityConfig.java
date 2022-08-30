package com.security.conf;

import com.security.conf.CustomerAccessDecisionManger;
import com.security.conf.CustomerFilterInvocationSecurityMetadataSource;
import com.security.filter.CaptchaFilter;
import com.security.filter.JwtAuthenticationFilter;
import com.security.handlers.*;
import com.security.service.iml.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginSuccessHandler loginSuccessHandler;
    @Autowired
    LoginFailureHandler loginFailureHandler;
    @Autowired
    CaptchaFilter captchaFilter;
    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    UserService userService;
    @Autowired
    JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager());
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Java数组初始化用的是{花括号}
    //URL白名单，访问时不需要拦截
    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/favicon.ico",
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //设置userDetail和加密方法
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()

                //登陆配置
                .formLogin()
                .successHandler(loginSuccessHandler)//登陆成功处理器
                .failureHandler(loginFailureHandler)//登录失败处理器

                //退出
                .and()
                .logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler)

                //设置不生成session策略
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()//所有人都可以访问
                .anyRequest().authenticated()//需要登陆，即需要认证

                //异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)//没有认证
                .accessDeniedHandler(jwtAccessDeniedHandler)//没有权限

                //配置自定义的过滤器=》前置过滤器
                .and()
                //captchaFilter 校验验证码，暂未开启
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                //jwtAuthenticationFilter jwt 认证
                .addFilterAt(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        //CustomerFilterInvocationSecurityMetadataSource
                        // 1.初始化所有路径所需的角色，放入redis中
                        // 2.获取请求路径，从redis中取得对应路径所需要角色，交给security框架
                        object.setSecurityMetadataSource(new CustomerFilterInvocationSecurityMetadataSource());
                        //CustomerAccessDecisionManger
                        // 1.获取用户角色信息循环比较对应的路径所需角色
                        object.setAccessDecisionManager(new CustomerAccessDecisionManger());
                        return object;
                    }
                });
    }
}
