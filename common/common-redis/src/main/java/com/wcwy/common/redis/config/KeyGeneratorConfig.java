package com.wcwy.common.redis.config;

/**
 * ClassName: KeyGeneratorConfig
 * Description:
 * date: 2023/11/21 11:04
 *
 * @author tangzhuo
 * @since JDK 1.8
 */


import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

/**
 * @Description 自定义Key生成器
 */
@Configuration
public class KeyGeneratorConfig {
    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                return method.getName() + Arrays.asList(objects).toString() + UUID.randomUUID();
            }
        };
    }
}


