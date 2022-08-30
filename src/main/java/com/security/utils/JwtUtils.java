package com.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
//通过配置文件赋值expire、secret
public class JwtUtils {
    //两个是参数
    private long expire;//保留天数
    private String secret;//密钥
    private String header;//传给前端

    //生成jwt
    public String generateToken(String username){
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() * expire);
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    //解析jwt
    public Claims getClaimByToken(String jwt){
        try{
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        }catch (Exception e){
            return null;
        }
    }

    //jwt是否过期
    public boolean isTokenExpired(Claims claims){
        //如果过期时间在当前时间之前，就代表过期
        return claims.getExpiration().before(new Date());
    }
}
