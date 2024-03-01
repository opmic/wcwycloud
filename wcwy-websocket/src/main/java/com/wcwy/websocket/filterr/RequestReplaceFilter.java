package com.wcwy.websocket.filterr;

/**
 * ClassName: RequestReplaceFilter
 * Description:
 * date: 2023/10/13 11:42
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.HandshakeRequest;

import java.io.BufferedReader;
import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@Slf4j
public class RequestReplaceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String contentType = request.getContentType();

        request = this.addTokenForWebSocket(request, response);

        filterChain.doFilter(request, response);
    }

    private HttpServletRequest addTokenForWebSocket(HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("Authorization");
        if(StrUtil.isNotBlank(token)) {
            return request;
        }
        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
        token = request.getHeader(HandshakeRequest.SEC_WEBSOCKET_PROTOCOL);
        if(StrUtil.isBlank(token)) {
            return request;
        }
        requestWrapper.addHeader("Authorization", token);
        response.addHeader(HandshakeRequest.SEC_WEBSOCKET_PROTOCOL, token);
        return  requestWrapper;
    }
}


