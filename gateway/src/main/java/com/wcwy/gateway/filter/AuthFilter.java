package com.wcwy.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.wcwy.common.base.exception.ServiceException;
import com.wcwy.common.redis.util.RedisService;
import com.wcwy.gateway.config.TokenHandleUtil;
import com.wcwy.gateway.constant.GlobalConstant;
import com.wcwy.gateway.enums.ClientTypeEnum;
import com.wcwy.gateway.enums.IpAddress;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 **/
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Autowired
    private TokenHandleUtil tokenHandleUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisService redisService;

    public static Mono<Void> errorInfo(ServerWebExchange exchange, String message) {
        // 自定义返回格式
        Map<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("code", 405);
        resultMap.put("message", message);
        resultMap.put("timestamp", System.currentTimeMillis());
        return Mono.defer(() -> {
            byte[] bytes = JSONObject.toJSONString(resultMap).getBytes();
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Flux.just(buffer));
        });
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String upgrade = exchange.getRequest().getHeaders().getUpgrade();
        if ("websocket".equals(upgrade)) {
            return chain.filter(exchange);
        }
        //如果是系统就放行

        ServerHttpRequest request = exchange.getRequest();
        System.out.println(request.getURI().getPath());
        System.out.println(IpUtil.getIpAddr(request));
        String path = request.getURI().getPath();

        List<String> system = request.getHeaders().get("system");
        if (system != null && system.size() > 0 && "system".equals(system.get(0))) {
            return chain.filter(exchange);
        }
        if (request.getURI().getPath().contains("oauth/check_token")
                || request.getURI().getPath().contains("system/version")
                /*  || request.getURI().getPath().contains("/file/images")*/
                || request.getURI().getPath().contains("wcwy-company/token/accessToken")
                || request.getURI().getPath().contains("system/config")
                || request.getURI().getPath().contains("/v2/api-docs")
                || request.getURI().getPath().contains("callback/tencent/ad/oauth2")
                || request.getURI().getPath().contains("oauth/oauth/token")
                || request.getURI().getPath().contains("wcwy-post/wxLogin/weixinLogin")
                || request.getURI().getPath().contains("wcwy-post/wx-pay/native/notify")
                || request.getURI().getPath().contains("wcwy-company/topUp/updateCurrencyCount")
                || request.getURI().getPath().contains("wcwy-company/mistake/**")) {
            return chain.filter(exchange);
        }
       /* List<String> tenantId = request.getHeaders().get(GlobalConstant.SYSTEM.TENANT_ID);
        if (null == tenantId || tenantId.size() != 1) {
            return errorInfo(exchange, "租户信息错误");
        }*/

        List<String> fromClient = request.getHeaders().get(GlobalConstant.SYSTEM.FROM_CLIENT);
        if (null == fromClient || fromClient.size() != 1) {
            return errorInfo(exchange, "客户端信息错误");
        }
        ClientTypeEnum clientTypeByCode = ClientTypeEnum.getClientTypeByCode(fromClient.get(0));
        if (clientTypeByCode == ClientTypeEnum.UNKNOWN) {
            return errorInfo(exchange, "客户端信息错误");
        }
        List<String> jwt = request.getHeaders().get(GlobalConstant.SYSTEM.TENANT_ID);
        if (null == jwt || jwt.size() != 1) {
            return errorInfo(exchange, "未携带访问token");
        }
        if (null != jwt || jwt.size() > 0) {
            String s = jwt.get(0);
            Claims claimsFromJwt = JWT.getClaimsFromJwt(s);
            if (claimsFromJwt == null) {
                return errorInfo(exchange, "非法token");
            }

            if(! "/wcwy-post/tCompanyPost/firmPost".equals(path)){
                s=s+path;
                //3次请求
                if (redisService.exists(s + "repetition")) {
                    if (redisService.exists(s + "repetition1")) {
                        if (redisService.exists(s + "repetition2")) {
                            if (redisService.exists(s + "repetition3")) {
                                if (redisService.exists(s + "repetition4")) {
                                    return errorInfo(exchange, "请勿重复请求!");
                                }
                                redisService.setEx(s + "repetition4", path, 1L);
                            }
                            redisService.setEx(s + "repetition3", path, 1L);
                        }
                        redisService.setEx(s + "repetition2", path, 1L);
                    }
                    redisService.setEx(s + "repetition1", path, 1L);
                }
                redisService.setEx(s + "repetition", path, 2L);
            }
        }
        List<String> list = request.getHeaders().get(GlobalConstant.SYSTEM.AUTHORIZATION);
        if (null != list && list.size() > 0) {
            String bearer = list.get(0).replace("Bearer", "").trim();
            //存储用户登录的ip地址
            String ip = IpUtil.getIpAddr(request);
            ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();

            String s = stringValueOperations.get(IpAddress.login_ip.getIpAddress() + bearer);
            if (!ip.equals(s)) {
                System.err.println("获取登录的ip"+ip);
                System.err.println("缓存的ip"+ip);
               // return errorInfo(exchange, "您已在其它电脑登陆,请重新登录!");
            }
            Boolean aBoolean = tokenHandleUtil.existAccessToken(bearer);
            if (!aBoolean) {
                return errorInfo(exchange, "身份已过期,请重新登录！");
            }
            tokenHandleUtil.getUsernameByToken(bearer);//刷新token有效期  避免直接退出
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


}
