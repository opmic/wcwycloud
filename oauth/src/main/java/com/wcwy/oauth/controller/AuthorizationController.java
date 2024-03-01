package com.wcwy.oauth.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;

import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

import static org.apache.naming.ResourceRef.AUTH;

/**
 * ClassName: AuthorizationController
 * Description:
 * date: 2023/6/20 16:36
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/authorization")
public class AuthorizationController {


    @Autowired
    private TokenStore tokenStore;

    /*   private OAuth2RequestValidator oAuth2RequestValidator = new DefaultOAuth2RequestValidator();
*/

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @PostMapping("/admin")
    public OAuth2AccessToken adminLogin(@RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        //创建客户端信息,客户端信息可以写死进行处理，因为Oauth2密码模式，客户端双信息必须存在，所以伪装一个
        //如果不想这么用，需要重写比较多的代码
        User clientUser = new User("c1", "wcwy", new ArrayList<>());
        String token1 = parameters.get("token").toString().trim();
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token1);
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        JSONObject jsonObject = JSON.parseObject(userAuthentication.getName());
        parameters.put("token",jsonObject.get("userMobile").toString());
        //生成已经认证的client
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(clientUser, null, new ArrayList<>());
       /* Map<String, String> parameters = new HashMap<String, String>();*/
        //封装成一个UserPassword方式的参数体，放入手机号
      /*  parameters.put("username", request.getPhone());
        //放入验证码
        parameters.put("password", request.getVcode());
        //授权模式为：密码模式
        parameters.put("grant_type", "password");*/
        //调用自带的获取token方法。
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(token, parameters).getBody();
        return oAuth2AccessToken;
    }



  /*  @RequestMapping(value = "/change", method = RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken> change(Principal principal, @RequestParam Map<String, String> parameters) {
        System.out.println(principal);
        System.out.println("111111111");
        String token1 = parameters.get("token").toString().trim();
        OAuth2Authentication token = tokenStore.readAuthentication(token1);
        System.out.println(token);

 *//*       if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException(
                    "There is no client authentication. Try adding an appropriate authentication filter.");
        }*//*

        String clientId = "c1";
        ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(clientId);
        OAuth2RequestFactory defaultOAuth2RequestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
        TokenRequest tokenRequest = defaultOAuth2RequestFactory.createTokenRequest(parameters, authenticatedClient);

        if (clientId != null && !clientId.equals("")) {
            // Only validate the client details if a client authenticated during this
            // request.
            if (!clientId.equals(tokenRequest.getClientId())) {
                // double check to make sure that the client ID in the token request is the same as that in the
                // authenticated client
                throw new InvalidClientException("Given client ID does not match authenticated client");
            }
        }
        if (authenticatedClient != null) {
            oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
        }
        if (!StringUtils.hasText(tokenRequest.getGrantType())) {
            throw new InvalidRequestException("Missing grant type");
        }
        if (tokenRequest.getGrantType().equals("implicit")) {
            throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
        }

        if (isAuthCodeRequest(parameters)) {
            // The scope was requested or determined during the authorization step
            if (!tokenRequest.getScope().isEmpty()) {
                tokenRequest.setScope(Collections.<String>emptySet());
            }
        }

        if (isRefreshTokenRequest(parameters)) {
            // A refresh token has its own default scopes, so we should ignore any added by the factory here.
            tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
        }

        OAuth2AccessToken token12 = grant(tokenRequest.getGrantType(), tokenRequest);
        if (token12 == null) {
            throw new UnsupportedGrantTypeException("Unsupported grant type");
        }

        return getResponse(token12);
        return null;
    }
    private boolean isRefreshTokenRequest(Map<String, String> parameters) {
        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
    }

    private boolean isAuthCodeRequest(Map<String, String> parameters) {
        return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
    }

    private ResponseEntity<OAuth2AccessToken> getResponse(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<OAuth2AccessToken>(accessToken, headers, HttpStatus.OK);
    }
*/

}
