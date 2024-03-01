package com.wcwy.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


/**
 * ClassName: CorsConfig
 * Description:跨域请求
 * date: 2022/9/1 14:58
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebFilter corsFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!CorsUtils.isCorsRequest(request)) {
                return chain.filter(exchange);
            }
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
            headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3628800");
            return chain.filter(exchange);
        };

    }

}
