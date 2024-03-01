package com.wcwy.gateway.filter;

/**
 * ClassName: HostAddrKeyResolver
 * Description:
 * date: 2023/3/23 14:58
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 根据ip地址进行限流
 *
 * @author yuanzhihao
 * @since 2022/4/27
 */
@Component
public class HostAddrKeyResolver implements KeyResolver {
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
    }
}

