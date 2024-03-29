package com.wcwy.gateway.config;

/**
 * ClassName: TokenHandleUtil
 * Description:
 * date: 2023/3/28 10:25
 *
 * @author tangzhuo
 * @since JDK 1.8
 */


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/*import javax.servlet.http.HttpServletRequest;*/

import java.util.Date;
import java.util.Map;

/**
 *
 * 类名称：TokenHandleUtil
 * 类描述：token处理工具类
 * 创建人：pansh
 * 创建时间：2021年12月22日 上午10:57:21
 *
 */
@Component
public class TokenHandleUtil {

    private static final Logger logger = LoggerFactory.getLogger(TokenHandleUtil.class);

    private static final String ACCESS = "access:";
    private static final String AUTH_TO_ACCESS = "auth_to_access:";
    private static final String AUTH = "auth:";

    /**
     * token剩余过期时间
     */
    @Value("${oauth.token.expires.remain}")
    private int expriesRemain;

    /**
     * token总过期时间
     */
    @Value("${oauth.token.expires.total}")
    private int expriesTotal;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    public String getAccessToken(String authorization){
        String accessToken = null;
        if(StringUtils.isEmpty(authorization)){
         /*   HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            authorization = request.getHeader("Authorization");*/
        }
        if (StringUtils.isNotEmpty(authorization)) {
            String[] auth = authorization.split(" ");
            if (auth.length == 2) {
                accessToken = auth[1];
            }
        }
        return accessToken;
    }

    /**
     * 判断token是否有效
     * @param authorization
     * @return
     */

  /*  @Autowired
    private TokenStore tokenStore;*/
    public Boolean   existAccessToken (String authorization){
       /* OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        tokenStore.removeAccessToken(accessToken);
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        tokenStore.removeRefreshToken(refreshToken);*/
        RedisConnection conn = getConnection();
        byte[] key = serializeKey(ACCESS + authorization);
        byte[] bytes = conn.get(key);
        DefaultOAuth2AccessToken accessToken = serializationStrategy.deserialize(bytes, DefaultOAuth2AccessToken.class);
        if(accessToken !=null){
            return true;
        }
        return false;
    }

    /**
     * 根据token获取用户名
     */
    public String getUsernameByToken(String authorization){
        String username = null;
        // authorization形式:4ae3b353-ded2-4796-a921-4b16ddede9e8
        String token = authorization;
        if (StringUtils.isNotEmpty(token)) {
            // 根据token获取username值
            RedisConnection conn = getConnection();
            try {
                byte[] key = serializeKey(ACCESS + token);
                byte[] bytes = conn.get(key);
                DefaultOAuth2AccessToken accessToken = serializationStrategy.deserialize(bytes, DefaultOAuth2AccessToken.class);
                if(accessToken != null){
                    Map<String, Object> userMap = accessToken.getAdditionalInformation();
                    logger.info("userMap={}", userMap);
                    if (userMap != null) {
                        username = (String) userMap.get("username");
                    }

                    int expiresIn = accessToken.getExpiresIn();
                    if (expiresIn < expriesRemain) {
                        logger.info("token续签, authorization={}", authorization);
                        Date newExpiration = new Date(System.currentTimeMillis() + expriesTotal*1000);
                        accessToken.setExpiration(newExpiration);
                        // 重置access中OAuth2AccessToken过期信息
                        conn.set(key, serializationStrategy.serialize(accessToken));
                        // 重置access、auth、uname_to_access的过期时间
                        conn.expire(key, expriesTotal);

                        byte[] authKey = serializeKey(AUTH + token);
                        conn.expire(authKey, expriesTotal);
                        if (StringUtils.isNotEmpty(username)) {
                            conn.expire(serializeKey("uname_to_access:browser:" + username), expriesTotal);
                        }

                        // 重置auth_to_access过期时间
                        bytes = conn.get(authKey);
                        OAuth2Authentication authentication = serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
                        AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
                        // 解析redis存储的authToAccess的token值
                        String authToAccessToken = authenticationKeyGenerator.extractKey(authentication);
                        byte[] authToAccessKey = serializeKey(AUTH_TO_ACCESS + authToAccessToken);

                        // 重置auth_to_access过期时间
                        bytes = conn.get(authToAccessKey);
                        DefaultOAuth2AccessToken authToAccessTokenObj = serializationStrategy.deserialize(bytes, DefaultOAuth2AccessToken.class);
                        authToAccessTokenObj.setExpiration(newExpiration);
                        // 重置auth_to_access中OAuth2AccessToken过期信息
                        conn.set(authToAccessKey, serializationStrategy.serialize(authToAccessTokenObj));
                        conn.expire(authToAccessKey, expriesTotal);
                    }
                }
            }finally{
                conn.close();
            }
        }
        return username;
    }

    /**
     * @Description: 记录token的ip地址
     * @param null 
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/3/28 14:35
     */
    
  public void   recordToken(){
        
    }
    
    private RedisConnection getConnection() {
        return redisConnectionFactory.getConnection();
    }

    private byte[] serializeKey(String object) {
        return serialize("" + object);
    }

    private byte[] serialize(String string) {
        return serializationStrategy.serialize(string);
    }
}

