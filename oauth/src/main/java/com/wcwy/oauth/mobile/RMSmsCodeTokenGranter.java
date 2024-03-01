package com.wcwy.oauth.mobile;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName: DemoSmsCodeTokenGranter
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-24 20:44
 */

public class RMSmsCodeTokenGranter extends AbstractTokenGranter {

    //@Autowired


    private static final String GRANT_TYPE = "rm_code";
    private RMUserDetailsService demoUserDetailsService;
    public RMSmsCodeTokenGranter(AuthorizationServerTokenServices endpoints, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, RMUserDetailsService userDetailsService) {
        this(endpoints,clientDetailsService,requestFactory, GRANT_TYPE);
        this.demoUserDetailsService = userDetailsService;
    }

    protected RMSmsCodeTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String mobile = parameters.get("mobile");
        String smsCode = parameters.get("sms_code");
        //TODO： 从redis中获取key为mobile的值是否匹配smsCode

        if(StringUtils.isEmpty(smsCode)){
            throw  new  UsernameNotFoundException("验证码不能为空!");
        }
        //验证验证码
         demoUserDetailsService.verificationCode(smsCode, mobile);
        //获取用户信息
        UserDetails userDetails = demoUserDetailsService.loadUserByMobile(mobile);
        if(userDetails==null){
            throw  new UsernameNotFoundException("该用户不存在");
        }
//		preAuthenticationChecks.check(userDetails);
 /*       Authentication userAuth = new UsernamePasswordAuthenticationToken(userDetails, null);
        //我们不需要使用AuthenticationManager认证userAuth，直接根据userAuth创建OAuth2Request
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);*/
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(userDetails, client,authorities);
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}

