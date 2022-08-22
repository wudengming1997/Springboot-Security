package com.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @BelongsProject: Springboot-Security
 * @BelongsPackage: com.security
 * @Classname SecurityApplication
 * @Description TODO
 * @Date 2022/8/20 11:36 上午
 * @Created by wudengming
 * @Version: 1.0
 */
@SpringBootApplication
public class SecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class,args);
    }
}
