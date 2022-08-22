package com.security.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: Springboot-Security
 * @BelongsPackage: com.security.controller
 * @Classname Controller
 * @Description TODO
 * @Date 2022/8/20 11:28 上午
 * @Created by wudengming
 * @Version: 1.0
 */
@RestController
public class Controller {
    @RequestMapping("/hello")
    public DefaultOAuth2User hello(){
        return (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }
}
