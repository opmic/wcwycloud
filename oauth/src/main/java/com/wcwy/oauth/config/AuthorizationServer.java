package com.wcwy.oauth.config;

import com.wcwy.oauth.granter.*;
import com.wcwy.oauth.mobile.*;
import com.wcwy.oauth.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName: AuthorizationServer
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-24 17:21
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthorizationServerTokenServices tokenService;
    @Autowired
    private AdUserDetailServiceImpl adUserDetailsServiceImpl;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private ADWXLoginDetailServiceImpl adwxLoginDetailService;
    @Autowired
    private AdRecommendDetailServiceImpl adRecommendDetailService;
    @Autowired
    private RMWXLoginDetailServiceImpl rmwxLoginDetailService;
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private AdJobhunterDetailServiceImpl adJobhunterDetailService;
    @Autowired
    private AdRecommendedCompaniesDetailServiceImpl adRecommendedCompaniesDetailService;

    //@Autowired
    // private DemoUserDetailsServiceImpl demoUserDetailsService;
    @Autowired
    private RCWXLoginDetailServiceImpl rcwxLoginDetailService;
    @Autowired
    private JBWXLoginDetailServiceImpl jbwxLoginDetailService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AdminDetailServiceImpl adminDetailService;
    @Autowired
    TokenStore tokenStore;
    @Autowired
    private DemoUserDetailsService demoUserDetailsService;
    @Autowired
    private RMUserDetailsService rmUserDetailsService;
    @Autowired
    private TJUserDetailsService tjUserDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;

    //将客户端信息存储到数据库
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    //客户端详情服务
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        clients.withClientDetails(clientDetailsService);
     /*   clients.inMemory()// 使用in-memory存储
                .withClient("c1")// client_id
               // .secret(new BCryptPasswordEncoder().encode("secret"))//客户端密钥
                .secret("secret")//客户端密钥
                .resourceIds("res1")//资源列表
                .authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token","ad_password")// 该client允许的授权类型authorization_code,password,refresh_token,implicit,client_credentials
                .scopes("all")// 允许的授权范围
                .autoApprove(false)//false跳转到授权页面
                //加上验证回调地址
                .redirectUris("http://www.baidu.com");*/
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);//设置授权码模式的授权码如何存取
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
     /*   endpoints
                .authenticationManager(authenticationManager)//认证管理器
                .authorizationCodeServices(authorizationCodeServices)//授权码服务
                .tokenServices(tokenService())//令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);*/

        List<TokenGranter> tokenGranters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        // 添加手机号加密码授权模式
        /*    tokenGranters.add(new DemoSmsCodeTokenGranter(endpoints, demoUserDetailsService));*/
        // 添加自定义授权模式（实际是密码模式的复制）
        tokenGranters.add(new AdPasswordTokenGranter(adUserDetailsServiceImpl, tokenService, endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), passwordEncoder));
        tokenGranters.add(new AdRecommendedCompaniesPasswordTokenGranter(adRecommendedCompaniesDetailService, tokenService, endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), passwordEncoder));
        tokenGranters.add(new RCWXLoginCompanyTokenGranter(rcwxLoginDetailService, tokenService, endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), passwordEncoder));
        tokenGranters.add(new AdminPasswordTokenGranter(adminDetailService, tokenService, endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), passwordEncoder));
        tokenGranters.add(new AdWXLoginJobhunterTokenGranter(jbwxLoginDetailService, tokenService, endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), passwordEncoder));
        tokenGranters.add(new AdJobhunterPasswordTokenGranter(adJobhunterDetailService, tokenService, endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), passwordEncoder));
        tokenGranters.add(new TJIdentityTokenGranter(adJobhunterDetailService, tokenService, endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), tokenStore,adRecommendDetailService,adUserDetailsServiceImpl));
        tokenGranters.add(new AdRecommendPasswordTokenGranter(adRecommendDetailService, tokenService, endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), passwordEncoder));
        tokenGranters.add(new AdWXLoginCompanyTokenGranter(adwxLoginDetailService, tokenService, endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), passwordEncoder));
        tokenGranters.add(new AdWXLoginRecommendTokenGranter(rmwxLoginDetailService, tokenService, endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), passwordEncoder));
        tokenGranters.add(new DemoSmsCodeTokenGranter(tokenService, endpoints.getClientDetailsService(),endpoints.getOAuth2RequestFactory(),demoUserDetailsService));
        tokenGranters.add(new RMSmsCodeTokenGranter(tokenService, endpoints.getClientDetailsService(),endpoints.getOAuth2RequestFactory(),rmUserDetailsService));
        tokenGranters.add(new TJSmsCodeTokenGranter(tokenService, endpoints.getClientDetailsService(),endpoints.getOAuth2RequestFactory(),tjUserDetailsService));
        endpoints.authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices)//授权码服务
                .tokenServices(tokenService)//令牌管理服务
                //.tokenServices(tokenServices())//令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
                .tokenGranter(new CompositeTokenGranter(tokenGranters)).reuseRefreshTokens(false);

    }



    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("permitAll()")                    //oauth/token_key是公开
                .checkTokenAccess("permitAll()")                  //oauth/check_token公开
                .allowFormAuthenticationForClients()                //表单认证（申请令牌）
        ;
    }

}
