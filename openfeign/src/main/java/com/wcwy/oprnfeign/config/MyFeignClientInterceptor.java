package com.wcwy.oprnfeign.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @ClassName: MyFeignClientInterceptor
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-26 10:14
 */
/*
 * Feign拦截器
 */
 @Component
public class MyFeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        try {
            // 获取对象
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (requestAttributes != null) {
                // 获取请求对象
                HttpServletRequest request = requestAttributes.getRequest();
                // 获取当前请求的header，获取到jwt令牌
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String headerName = headerNames.nextElement();
                        String headerValue = request.getHeader(headerName);
                        // 将header向下传递
                        template.header(headerName, headerValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //注册Feign拦截器
    @Bean
    public MyFeignClientInterceptor feignClientInterceptor() {
        return new MyFeignClientInterceptor();
    }


}

