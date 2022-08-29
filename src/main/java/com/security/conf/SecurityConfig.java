package com.security.conf;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.service.iml.UserService;
import com.security.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于数据库的自定义用户认证
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource
	private UserService userService;

	/**
	 * 密码加密方式，必须指定一种
	 * 本例使用官方推荐的BCrypt强哈希函数，密钥迭代次数为2^strength，strength取值在4~31之间，默认为10
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(15);
	}

	/**
	 * 认证设置（配置用户信息：登录名、登录密码、所属角色）
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService);
	}

	/**
	 * 认证设置（HttpSecurity认证，用户请求URL认证）
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("----------------> MyWebSecurityConfig.configure(HttpSecurity)");
		// authorizeRequests()方法表示开启HttpSecurity认证
		http.authorizeRequests()
				// HttpSecurity配置，可以通过URL获取对应的角色信息
				.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
					@Override
					public <O extends FilterSecurityInterceptor> O postProcess(O object) {
						object.setSecurityMetadataSource(new CustomerFilterInvocationSecurityMetadataSource());
						object.setAccessDecisionManager(new CustomerAccessDecisionManger());
						return object;
					}
				})
				.and()
				// 开启表单登录，即登录页
				.formLogin()
				// 自定义登录页，未配置下启用默认登录页
				// .loginPage("/login_page")
				// 登录请求接口，默认为 "/login" 接口
				.loginProcessingUrl("/login")
				// 登录参数-用户名
				.usernameParameter("username")
				// 登录参数-密码
				.passwordParameter("password")
				// 登录成功回调函数-返回登陆成功的JSON信息
				.successHandler((request, response, authentication) -> {
					response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
					PrintWriter out = response.getWriter();
					response.setStatus(HttpServletResponse.SC_OK);
					Map<String, Object> map = new HashMap<>();
					Object principal = authentication.getPrincipal();
					map.put("status", HttpServletResponse.SC_OK);
					map.put("msg", principal);
					ObjectMapper om = new ObjectMapper();
					out.write(om.writeValueAsString(map));
					out.flush();
					out.close();
					System.out.println("-----login_success-----> " + JSON.toJSONString(map));
					this.getCookieMsg(request);
				})
				// 登录失败回调函数-返回登陆失败的JSON信息
				.failureHandler((request, response, e) -> {
					response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
					PrintWriter out = response.getWriter();
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					Map<String, Object> map = new HashMap<>();
					map.put("status", HttpServletResponse.SC_UNAUTHORIZED);
					if (e instanceof LockedException) {
						map.put("msg", "账户被锁定，登陆失败！");
					} else if (e instanceof BadCredentialsException) {
						map.put("msg", "账户名或密码输入错误，登陆失败！");
					} else if (e instanceof DisabledException) {
						map.put("msg", "账户被禁用，登陆失败！");
					} else if (e instanceof AccountExpiredException) {
						map.put("msg", "账户已过期，登陆失败！");
					} else if (e instanceof CredentialsExpiredException) {
						map.put("msg", "密码已过期，登陆失败！");
					} else {
						map.put("msg", "登陆失败！");
					}
					ObjectMapper om = new ObjectMapper();
					out.write(om.writeValueAsString(map));
					out.flush();
					out.close();
					System.out.println("-----login_failure-----> " + JSON.toJSONString(map));
				})
				// 和登录相关的接口都不需要认证即可访问
				.permitAll()
				.and()
				// 开启注销登录配置
				.logout()
				// 配置注销登录请求URL，默认为 "/logout"
				.logoutUrl("/logout")
				// 是否清除身份认证信息，默认为true，表示清除
				.clearAuthentication(true)
				// 是否使session失效，默认为true
				.invalidateHttpSession(true)
				// 删除指定的Cookie信息，可以传入多个key
				.deleteCookies("JSESSIONID")
				// 注销回调函数，可以处理数据清除工作
				.addLogoutHandler((request, response, authentication) -> {
					System.out.println("-----logout_handler----->");
					this.getCookieMsg(request);
				})
				// 注销成功回调函数
				.logoutSuccessHandler((request, response, authentication) -> {
					System.out.println("-----logout_success----->");
					// 注销成功后重定向到登录页
					response.sendRedirect("/login");
				})
				.and()
				// 关闭csrf
				.csrf()
				.disable();
	}

	/**
	 * 自定义方法，获取Cookie信息
	 */
	private void getCookieMsg(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (null == cookies || cookies.length < 1) {
			System.out.println("------> Cookie已全部清除！");
		} else {
			System.out.println("---Cookie---> " + JSON.toJSONString(cookies));
		}
	}
}
