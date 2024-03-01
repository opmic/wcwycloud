package com.wcwy.common.web.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author： 乐哥聊编程(全平台同号)
 */
@Configuration
public class FeignConfig  implements RequestInterceptor{
    @Value("${spring.application.name}")
    private String applicationName;
    /**
     * 让DispatcherServlet向子线程传递RequestContext
     *
     * @param servlet servlet
     * @return 注册bean
     */
    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration(DispatcherServlet servlet) {
        servlet.setThreadContextInheritable(true);
        return new ServletRegistrationBean<>(servlet, "/**");
    }

    /**
     * 覆写拦截器，在feign发送请求前取出原来的header并转发
     *
     * @return 拦截器
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return (template) -> {
            ServletRequestAttributes
                    attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            //获取请求头
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    //将请求头保存到模板中
                    if (!name.equalsIgnoreCase("serviceName")){
                        template.header(name, values);
                    }

                }
                template.header("serviceName",applicationName);
            }

        };
    }
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    } /*FULL详细日志*/

   /* @Override
    public void apply(RequestTemplate requestTemplate) {
*//*        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String accessToken = request == null ? StringUtils.EMPTY : request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("=================Feign Interceptor AccessToken: " + accessToken);
        requestTemplate.header(HttpHeaders.AUTHORIZATION, accessToken);*//*
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

    }*/

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes == null) {
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));//为空进行新建
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        // 对消息头进行配置
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            /**
             * 判断有没有token，如果是定时任务进来，是没有token的，此时用默认token以调用到对应的Feign服务
             */
            boolean flag = true;
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                if(name.equals("token")){
                    flag = false;
                }
                template.header(name, values);
            }
            if(flag){
                template.header("token","!@#$%^&*()Cid6032001_Feign");
            }
        }
        // 对请求体进行配置
        Enumeration<String> bodyNames = request.getParameterNames();
        StringBuffer body =new StringBuffer();
        if (bodyNames != null) {
            while (bodyNames.hasMoreElements()) {
                String name = bodyNames.nextElement();
                String values = request.getParameter(name);
                body.append(name).append("=").append(values).append("&");
            }
        }
        if(body.length()!=0) {
            body.deleteCharAt(body.length()-1);
            template.body(body.toString());
        }
    }

}