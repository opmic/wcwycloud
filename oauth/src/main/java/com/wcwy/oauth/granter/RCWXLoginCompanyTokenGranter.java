package com.wcwy.oauth.granter;

import com.wcwy.common.base.constant.Constanst;
import com.wcwy.common.base.utils.AesUtil;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.oauth.service.ADWXLoginDetailServiceImpl;
import com.wcwy.oauth.service.RCWXLoginDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class RCWXLoginCompanyTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "rc_WXLogin";

    private RCWXLoginDetailServiceImpl userDetailsService;
    private PasswordEncoder bCryptPasswordEncoder;

    public RCWXLoginCompanyTokenGranter(RCWXLoginDetailServiceImpl userDetailsService,
                                        AuthorizationServerTokenServices tokenServices,
                                        ClientDetailsService clientDetailsService,
                                        OAuth2RequestFactory requestFactory,
                                        PasswordEncoder bCryptPasswordEncoder) {
        this(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    protected RCWXLoginCompanyTokenGranter(AuthorizationServerTokenServices tokenServices,
                                           ClientDetailsService clientDetailsService,
                                           OAuth2RequestFactory requestFactory,
                                           String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String code = parameters.get("code");
        String state = parameters.get("state");
        // 验证state为了用于防止跨站请求伪造攻击
        if (code != null && state != null) {
            String decrypt = AesUtil.decrypt(AesUtil.parseHexStr2Byte(state), AesUtil.PASSWORD_SECRET_KEY, 16);
            if (!decrypt.equals(Constanst.PWD_MD5 + DateUtils.getYYYYMMdd())) {
                throw new UsernameNotFoundException("state错误！");
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(code);
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
     /*   ArrayList<SimpleGrantedAuthority> objects = new ArrayList<>();
        SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority("p1");
        objects.add(simpleGrantedAuthority);*/
            UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(userDetails, client, authorities);
            OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        }
        return null;
    }


    public static void main(String[] args) {

    }
}
