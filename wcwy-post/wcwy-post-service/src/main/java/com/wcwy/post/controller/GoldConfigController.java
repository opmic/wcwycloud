package com.wcwy.post.controller;

import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.post.entity.GoldConfig;
import com.wcwy.post.entity.TPayConfig;
import com.wcwy.post.service.GoldConfigService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * ClassName: GoldConfigController
 * Description:
 * date: 2023/7/11 11:47
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@Api(tags = "金币配置接口")
@RequestMapping("/goldConfig")
@Slf4j
public class GoldConfigController {

    @Autowired
    private GoldConfigService goldConfigService;
    @Autowired
    private RedisUtils redisUtils;
    @PostConstruct
    public void init() {
        redisUtils.del(RedisCache.GOLD_CACHE.getValue());
        List<GoldConfig> list = goldConfigService.list();
        GoldConfig goldConfig = list.get(0);

        /* if(tPayConfig !=null){
         *//*  tPayConfig.setGradeA(tPayConfig.getGradeA().);*//*
        }*/
        redisUtils.set(RedisCache.GOLD_CACHE.getValue(), goldConfig);
    }
}
