package com.wcwy.gateway.filter;

import com.wcwy.gateway.constant.GlobalConstant;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: IpLimitFilter
 * Description:
 * date: 2023/3/23 15:10
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class IpLimitFilter implements GlobalFilter, Ordered {


    public static final String WARNING_MSG = "请求过于频繁，请稍后再试";

    public static final Map<String, Bucket> LOCAL_CACHE = new ConcurrentHashMap<>();

    // 令牌桶初始容量
    public static final long capacity = 30;

    // 补充桶的时间间隔，即2秒补充一次
    public static final long seconds = 1;

    // 每次补充token的个数
    public static final long refillTokens = 2;
    /**
     * 限流时间,单位秒
     */
    public int time = 3;

    /**
     * 限流次数
     */
    public int count =25;

    private RedisTemplate<Object, Object> redisTemplate;

    private RedisScript<Long> limitScript;

    @Autowired
    @Qualifier("redisTemplate1")
    public void setRedisTemplate1(RedisTemplate<Object, Object> redisTemplate1)
    {
        this.redisTemplate = redisTemplate1;
    }

    @Autowired
    public void setLimitScript(RedisScript<Long> limitScript)
    {
        this.limitScript = limitScript;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String upgrade = exchange.getRequest().getHeaders().getUpgrade();
        if("websocket".equals(upgrade)){
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        List<String> jwt = request.getHeaders().get(GlobalConstant.SYSTEM.TENANT_ID);
        if (null == jwt || jwt.size() != 1) {
            return chain.filter(exchange);
        }
        String combineKey = getCombineKey(jwt.get(0));
        List<Object> keys = Collections.singletonList(combineKey);
            Long number = redisTemplate.execute(limitScript, keys, count, time);
            if (StringUtils.isEmpty(number) || number.intValue() > count) {
                byte[] bits = WARNING_MSG.getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(bits);
                //指定编码，否则在浏览器中会中文乱码
                response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
                return response.writeWith(Mono.just(buffer));
            }
        // 直接放行
        return chain.filter(exchange);
    }

      /*  ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String ip = IpUtil.getIpAddr(request);

        log.info("访问IP为:{}", ip);
   log.info("存储桶:{}", LOCAL_CACHE.size());
        List<String> jwt = request.getHeaders().get(GlobalConstant.SYSTEM.TENANT_ID);
        if (null == jwt || jwt.size() != 1) {
            return chain.filter(exchange);
        }
        Bucket bucket = LOCAL_CACHE.computeIfAbsent(jwt.get(0), k -> createNewBucket(jwt.get(0)));

        log.info("IP:{} ,令牌通可用的Token数量:{} ", jwt.get(0), bucket.getAvailableTokens());

        if (bucket.tryConsume(1)) {
            // 直接放行
            return chain.filter(exchange);
        } else {

            byte[] bits = WARNING_MSG.getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bits);
            //指定编码，否则在浏览器中会中文乱码
            response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
        }
*/


    @Override
    public int getOrder() {
        return 1;
    }
    public String getCombineKey( String token)
    {
        StringBuffer stringBuffer = new StringBuffer(RateLimiter.key);
            stringBuffer.append(token).append("-");
        return stringBuffer.toString();
    }

    private Bucket createNewBucket(String ip) {
        Duration refillDuration = Duration.ofSeconds(seconds);
        Refill refill = Refill.of(refillTokens, refillDuration);
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }
}
