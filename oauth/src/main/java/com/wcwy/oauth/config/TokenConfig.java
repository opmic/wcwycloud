package com.wcwy.oauth.config;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.apache.naming.ResourceRef.SCOPE;

/**
 * @ClassName: TokenConfig
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-24 17:22
 */
@Configuration
public class TokenConfig extends AuthorizationServerConfigurerAdapter {
    //private String SIGNING_KEY = "uaa123";

    public static final String SIGNING_KEY = "tangzhuo";

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private TokenStore tokenStore;
        /**
     * token存储策略配置
     */
    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
      /*  redisTokenStore.setAuthenticationKeyGenerator( new AuthenticationKeyGenerator());*/
       /* return new RedisTokenStore(redisConnectionFactory);*/
        return  redisTokenStore;
     /*   return new JwtTokenStore(accessTokenConverter());*/
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //对称秘钥，资源服务器使用该秘钥来验证
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }
    //令牌的管理服务

    @Bean
    @Primary
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        //客户端详情服务
        service.setClientDetailsService(clientDetailsService);
        //支持刷新令牌
        service.setSupportRefreshToken(true);
        //令牌存储策略
        service.setTokenStore(tokenStore());

        service.setReuseRefreshToken(false);
        //令牌增强
  /*      TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter()));
        service.setTokenEnhancer(tokenEnhancerChain);*/

        // 设置令牌有效期24小时
        service.setAccessTokenValiditySeconds(24 * 60 * 60 * 60);
        // 刷新令牌默认有效期3天
        service.setRefreshTokenValiditySeconds(7 * 24 * 60 * 60);
        // 当令牌被刷新时，它将用于（如果提供的话）检查用户身份验证。
        service.setAuthenticationManager(authenticationManager);
        return service;
    }


    private class AuthenticationKeyGenerator extends DefaultAuthenticationKeyGenerator {

        private static final String CLIENT_ID = "client_id";

        private static final String SCOPE = "scope";

        private static final String USERNAME = "username";

        private static final String CODE = "code";

        @Override
        public String extractKey(OAuth2Authentication authentication) {
            Map<String, String> values = new LinkedHashMap<String, String>();
            OAuth2Request authorizationRequest = authentication.getOAuth2Request();
            if (!authentication.isClientOnly()) {
                values.put(USERNAME, authentication.getName());
            }
            values.put(CLIENT_ID, authorizationRequest.getClientId());
            if (authorizationRequest.getScope() != null) {
                values.put(SCOPE, OAuth2Utils.formatParameterList(new TreeSet<String>(authorizationRequest.getScope())));
            }
            UUID uuid4 = UUID.randomUUID();
            values.put(CODE, uuid4.toString());
           /* OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);*/
           /* System.out.println(existingAccessToken);*/
            return generateKey(values);
        }
    }

}
