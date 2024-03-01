package com.wcwy.oauth.granter;

import com.wcwy.oauth.service.AdJobhunterDetailServiceImpl;
import com.wcwy.oauth.service.AdRecommendedCompaniesDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 短信验证码授权
 * 实际上就是复制ResourceOwnerPasswordTokenGranter中的代码，进行getOAuth2Authentication方法部分修改，
 * 这个类有点类似SpringSecurity中的各种AuthenticationFilter，也就是类似过滤器的作用
 *
 * @author lmabbe
 * @data 2021/5/11 20:14
 */
@Slf4j
public class AdRecommendedCompaniesPasswordTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "rc_password";
    private AdRecommendedCompaniesDetailServiceImpl userDetailsService;
    private PasswordEncoder bCryptPasswordEncoder;

    public AdRecommendedCompaniesPasswordTokenGranter(AdRecommendedCompaniesDetailServiceImpl userDetailsService,
                                                      AuthorizationServerTokenServices tokenServices,
                                                      ClientDetailsService clientDetailsService,
                                                      OAuth2RequestFactory requestFactory,
                                                      PasswordEncoder bCryptPasswordEncoder) {
        this(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    protected AdRecommendedCompaniesPasswordTokenGranter(AuthorizationServerTokenServices tokenServices,
                                                         ClientDetailsService clientDetailsService,
                                                         OAuth2RequestFactory requestFactory,
                                                         String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String username = parameters.get("username");
        String password = parameters.get("password");

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(!bCryptPasswordEncoder.matches(password,userDetails.getPassword())){
            throw new InvalidGrantException("账号密码错误");
        }
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(userDetails, client,authorities);
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }

}
