package com.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.springframework.security.core.context.SecurityContextHolder.*;

/**
 * @BelongsProject: Springboot-Security
 * @BelongsPackage: PACKAGE_NAME
 * @Classname com.security.SecurityApplication
 * @Description TODO
 * @Date 2022/8/22 5:06 下午
 * @Created by wudengming
 * @Version: 1.0
 */
@SpringBootApplication
public class SecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        // 创建redisTemplate 模版
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        // 关联redisConnectionFactory
        template.setConnectionFactory(factory);
        // 序列化key和value
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer(Object.class));
        template.afterPropertiesSet();
        return template;
    }
}
