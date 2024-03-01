package com.wcwy.post.controller;

import com.alibaba.fastjson2.JSON;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.config.CacheClient;
import com.wcwy.post.entity.ResumePaymentConfig;
import com.wcwy.post.entity.TPayConfig;
import com.wcwy.post.service.ResumePaymentConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: ResumePaymentConfigController
 * Description:
 * date: 2023/9/7 9:23
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@Api(tags = "简历付配置接口")
@RequestMapping("/resumePaymentConfig")
public class ResumePaymentConfigController {

    @Autowired
    private ResumePaymentConfigService resumePaymentConfigService;
    @Autowired
    private CacheClient cacheClient;
    @Autowired
    private RedisUtils redisUtils;

    @ApiOperation("获取简历付服务配置")
    @GetMapping("/select")
   /* @Cacheable(value="com:resumePaymentConfig:cache:list")*/
    public R<ResumePaymentConfig> select(){
        List<ResumePaymentConfig> list = resumePaymentConfigService.selectList();
        return R.success(list);
    }

    @GetMapping("/cache")
    public List<ResumePaymentConfig> cache(){
        List<ResumePaymentConfig> list = resumePaymentConfigService.selectList();
        return list;
    }

 /*   @PostConstruct
    public void init() {
        cacheClient.deleteCache(Cache.RESUME_PAYMENT_CONFIG.getKey(),null);

        List<ResumePaymentConfig> list = resumePaymentConfigService.list();
        *//* if(tPayConfig !=null){
         *//**//*  tPayConfig.setGradeA(tPayConfig.getGradeA().);*//**//*
        }*//*
       // cacheClient.setWithLogicalExpire(Cache.RESUME_PAYMENT_CONFIG.getKey(),list,2L, TimeUnit.MINUTES);
*//*        cacheClient.queryWithLogicalExpireList(Cache.RESUME_PAYMENT_CONFIG.getKey(),null,ResumePaymentConfig.class,resumePaymentConfigService::list,2L, TimeUnit.MINUTES)*//*
    }*/
}
