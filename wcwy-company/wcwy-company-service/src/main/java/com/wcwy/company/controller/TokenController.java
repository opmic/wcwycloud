package com.wcwy.company.controller;

import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.IpUtils;
import com.wcwy.common.base.utils.JWT;
import com.wcwy.common.base.utils.UUID;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName: TokenController
 * Description:
 * date: 2023/3/27 15:48
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/token")
@Api(tags = "token处理")
public class TokenController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TokenStore tokenStore;
    @GetMapping("/revokeToken")
    @ApiOperation("退出")
    @Log(title = "退出", businessType = BusinessType.UPDATE)
    public void revokeToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
            OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
            tokenStore.removeRefreshToken(refreshToken);
        }
    }
    @GetMapping("/accessToken")
    @ApiOperation("访问令牌")
    public R accessToken(HttpServletRequest request) {
        String s = JWT.generateJwtToken(IpUtils.getIpAddrcccc(request));
        return R.success(s);
    }

}
