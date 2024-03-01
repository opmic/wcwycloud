package com.wcwy.post.controller;

import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.post.entity.TPayConfig;
import com.wcwy.post.service.TPayConfigService;
import com.wcwy.post.vo.TPayConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【t_pay_config(支付兑换配置表)】的数据库操作Mapper
 * @createDate 2022-10-19 11:11:42
 * @Entity com.wcwy.post.entity.TPayConfig
 */
@RestController
@Api(tags = "支付兑换配置接口")
@RequestMapping("/tRefundInfo")
//@PreAuthorize("hasAnyAuthority('admin')")
@Slf4j
public class TPayConfigController {

    @Resource
    private TPayConfigService tPayConfigService;
 /*   @Autowired
    private RedisTemplate redisTemplate;*/
    @Autowired
    private RedisUtils redisUtils;
    @PostMapping("/updateTPay")
    @PreAuthorize("hasAnyAuthority('admin')")
    @ApiOperation("支付兑换配置修改")
    public R updateTPay(@Valid @RequestBody TPayConfigVO tPayConfigVO) {
        TPayConfig tPayConfig = new TPayConfig();
        BeanUtils.copyProperties(tPayConfigVO, tPayConfig);
        tPayConfig.setUpdateTime(LocalDateTime.now());
        boolean b = tPayConfigService.updateById(tPayConfig);
        if (b) {
            init();
            return R.success("更新成功");
        }
        return R.fail("更新失败");
    }
    @GetMapping("/selectTPayConfig")
    @ApiOperation("获取支付兑换配置数据")
    public R<TPayConfig>  selectTPayConfig(){
        TPayConfig tPayConfig = (TPayConfig) redisUtils.get(RedisCache.TPAYCONFIG.getValue());
        if(tPayConfig==null){
            init();
        }
        return R.success(tPayConfig);
    }


    @PostConstruct
    public void init() {
       redisUtils.del(RedisCache.TPAYCONFIG.getValue());
        List<TPayConfig> tPayConfigs = tPayConfigService.list();
        TPayConfig tPayConfig = tPayConfigs.get(0);
       /* if(tPayConfig !=null){
          *//*  tPayConfig.setGradeA(tPayConfig.getGradeA().);*//*
        }*/
        redisUtils.set(RedisCache.TPAYCONFIG.getValue(), tPayConfig);
    }

    @GetMapping("select")
    public TPayConfig select(){
        List<TPayConfig> tPayConfigs = tPayConfigService.list();
        TPayConfig tPayConfig = tPayConfigs.get(0);
        /* if(tPayConfig !=null){
         *//*  tPayConfig.setGradeA(tPayConfig.getGradeA().);*//*
        }*/
        redisUtils.set(RedisCache.TPAYCONFIG.getValue(), tPayConfig);
        return tPayConfig;
    }
    /*public static void main(String[] args) {
        Integer a=5;
        double c=a*0.01;
        System.out.println(c);
    }*/
}




