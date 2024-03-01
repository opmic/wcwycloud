package com.wcwy.common.redis.config;

/**
 * ClassName: CustomCacheResolver
 * Description:
 * date: 2023/11/21 11:05
 *
 * @author tangzhuo
 * @since JDK 1.8
 */


import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.*;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @Description 自定义缓存解析器
 */
@Slf4j
public class CustomCacheResolver extends SimpleCacheResolver {
    public CustomCacheResolver(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        // 拿到缓存
        Collection<? extends Cache> caches = super.resolveCaches(context);
        Object target = context.getTarget();
        BasicOperation operation = context.getOperation();
        Method method = context.getMethod();
        Object[] args = context.getArgs();
        ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();
        EvaluationContext evaluationContext = new MethodBasedEvaluationContext(context.getOperation(), context.getMethod(), context.getArgs(), paramNameDiscoverer);
        Expression expression = (new SpelExpressionParser()).parseExpression(((CacheableOperation) context.getOperation()).getKey());
        // 获取所有的缓存的名字
        context.getOperation().getCacheNames().forEach(cacheName -> {
            log.info("缓存的name:{}", cacheName);
            String key = cacheName + ':' + expression.getValue(evaluationContext, String.class);
            log.info("缓存的key全路径：{}", key);
        });
        // 返回缓存
        return caches;
    }
}


