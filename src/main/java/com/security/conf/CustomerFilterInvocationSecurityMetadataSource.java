package com.security.conf;

import com.alibaba.fastjson.JSON;
import com.security.dao.MenuMapper;
import com.security.entity.Menu;
import com.security.entity.Role;
import com.security.service.RoleService;
import com.security.utils.RedisUtil;
import com.security.utils.SpringUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.awt.SystemColor.menu;

/**
 * 自定义FilterInvocationSecurityMetadataSource
 */
@Component
public class CustomerFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Resource
    private RoleService roleService;
    private static boolean flag;

   /**
    * @Author wudengming
    * @Description 权限信息初始化到redis
    * @Date  2022/8/29
    * @Param []
    * @return void
   **/
    @PostConstruct
    private void initializeRolePermissions() {
        flag = roleService.addAllMenuRoleToRedis();
    }

   /**
    * @Author wudengming
    * @Description 通过传入路径获取访问该路径所需要的角色
    * @Date  2022/8/29
    * @Param [o]
    * @return java.util.Collection<org.springframework.security.access.ConfigAttribute>
   **/
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String url = ((FilterInvocation) o).getRequestUrl();

        RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);
        String roleString = String.valueOf(redisUtil.get(url));
        if (!StringUtils.isEmpty(roleString) && !"null".equals(roleString)) {
            String[] split = null;
            split = roleString.split(",");
            return SecurityConfig.createList(split);
        } else {
            return SecurityConfig.createList("ROLE_LOGIN");
        }
    }

    /**
     * @Author wudengming
     * @Description 返回所有定义好的权限资源 Spring Security在启动时会校验相关配置是否正确，如果不需要校验，直接返回null
     * @Date  2022/8/29
     * @Param []
     * @return java.util.Collection<org.springframework.security.access.ConfigAttribute>
    **/
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * 是否支持校验
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
