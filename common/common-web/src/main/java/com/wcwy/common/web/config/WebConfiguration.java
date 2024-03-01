package com.wcwy.common.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * ClassName: WebConfiguration
 * Description:
 * date: 2023/7/3 9:27
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Resource
    private AutoIdempotentInterceptor autoIdempotentInterceptor;

    /** * 添加拦截器 * @param registry */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(autoIdempotentInterceptor);
        super.addInterceptors(registry);
    }
}