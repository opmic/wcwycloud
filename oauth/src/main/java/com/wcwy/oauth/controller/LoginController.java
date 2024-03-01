package com.wcwy.oauth.controller;

import com.wcwy.oauth.utlis.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * ClassName: LoginController
 * Description:
 * date: 2023/8/3 20:24
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/oauth")
public class LoginController {

    @Autowired
    private TokenEndpoint tokenEndpoint;
    @PostMapping("/token")
    public ResponseEntity<OAuth2AccessToken> login(HttpServletRequest request, Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        String ip = IpUtil.getIpAddr(request);
        parameters.put("ip",ip);
        ResponseEntity<OAuth2AccessToken> oAuth2AccessTokenResponseEntity = tokenEndpoint.postAccessToken(principal, parameters);
        return oAuth2AccessTokenResponseEntity;
    }
}
