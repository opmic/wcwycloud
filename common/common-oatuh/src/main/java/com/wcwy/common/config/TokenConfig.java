package com.wcwy.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration
public class TokenConfig {

    private String SIGNING_KEY = "tangzhuo";
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * token存储策略配置
     */


    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
        //JWT令牌存储方案
       /* return new JwtTokenStore(accessTokenConverter());*/
    }
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //对称秘钥，资源服务器使用该秘钥来验证
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }

}
