package com.wcwy.oprnfeign.config;

/**
 * @ClassName: FeignConfig
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-26 16:14
 */

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Description feign客户端配置-设置自定义请求头
 * @ClassName FeignConfig
 * @Author 康世行
 * @Date 11:24 2022/6/23
 * @Version 1.0
 **/
@Configuration
public class FeignConfig implements RequestInterceptor {



    @Override
    public void apply(RequestTemplate requestTemplate) {
/*        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String accessToken = request == null ? StringUtils.EMPTY : request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("=================Feign Interceptor AccessToken: " + accessToken);
        requestTemplate.header(HttpHeaders.AUTHORIZATION, accessToken);*/
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) {
            return;
        }
        //处理上游请求头信息，传递时继续携带
        while (headerNames.hasMoreElements()) {

            String name = headerNames.nextElement();
            String values = request.getHeader(name);
            System.out.println("=================Feign Interceptor: "+name+"========" + values);
            requestTemplate.header(name, values);
        }
    }
}
