package com.security.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @BelongsProject: Springboot-Security
 * @BelongsPackage: com.security.conf
 * @Classname SecurityConfig
 * @Description TODO
 * @Date 2022/8/20 11:32 上午
 * @Created by wudengming
 * @Version: 1.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().anyRequest().authenticated().and().oauth2Login();
    }
}
