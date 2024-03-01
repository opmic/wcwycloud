package com.wcwy.oauth.granter;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wcwy.common.base.result.R;
import com.wcwy.oauth.service.AdJobhunterDetailServiceImpl;
import com.wcwy.oauth.service.AdRecommendDetailServiceImpl;
import com.wcwy.oauth.service.AdUserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.util.StringUtils;

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
public class TJIdentityTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "identity";

    private TokenStore tokenStore;
    private AdUserDetailServiceImpl adUserDetailService;
    private AdJobhunterDetailServiceImpl adJobhunterDetailService;
    private AdRecommendDetailServiceImpl adRecommendDetailService;

    public TJIdentityTokenGranter(AdJobhunterDetailServiceImpl userDetailsService,
                                  AuthorizationServerTokenServices tokenServices,
                                  ClientDetailsService clientDetailsService,
                                  OAuth2RequestFactory requestFactory,
                                  TokenStore tokenStore,AdRecommendDetailServiceImpl adRecommendDetailService, AdUserDetailServiceImpl adUserDetailService) {
        this(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.adJobhunterDetailService = userDetailsService;
        this.adRecommendDetailService = adRecommendDetailService;
        this.adUserDetailService = adUserDetailService;
        this.tokenStore = tokenStore;
    }

    protected TJIdentityTokenGranter(AuthorizationServerTokenServices tokenServices,
                                     ClientDetailsService clientDetailsService,
                                     OAuth2RequestFactory requestFactory,
                                     String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String token1 = parameters.get("token").trim();
        if(StringUtils.isEmpty(token1)){
            throw new InvalidGrantException("请传入token");
        }
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token1);
        if(oAuth2Authentication==null){
            throw new InvalidGrantException("请传入有效token");
        }
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        JSONObject jsonObject = JSON.parseObject(userAuthentication.getName());
        parameters.put("token",jsonObject.get("userMobile").toString());
        String username = parameters.get("token");
        String identity=parameters.get("identity");
        /*String password = parameters.get("password");*/
        UserDetails userDetails=null;
        if("TJ".equals(identity.trim())){
            userDetails  = adJobhunterDetailService.loadUserByUsername(username);
        }else  if("TR".equals(identity.trim())){
            userDetails  = adRecommendDetailService.loadUserByUsername(username);
        }else if("TC".equals(identity.trim())){
            userDetails  = adUserDetailService.loadUserByUsername(username);
        }else {
            throw new InvalidGrantException("请选择切换的身份!");
        }

      /*  if(!bCryptPasswordEncoder.matches(password,userDetails.getPassword())){
            throw new InvalidGrantException("账号密码错误");
        }*/
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(userDetails, client,authorities);
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
   /*     OAuth2AccessToken accessToken = tokenStore.readAccessToken(token1);
        if (accessToken != null) {
            // 移除access_token
            tokenStore.removeAccessToken(accessToken);

            // 移除refresh_token
            if (accessToken.getRefreshToken() != null) {
                tokenStore.removeRefreshToken(accessToken.getRefreshToken());
            }

        }*/

        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }

}
