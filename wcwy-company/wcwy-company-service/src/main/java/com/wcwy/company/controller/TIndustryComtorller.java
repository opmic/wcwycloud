package com.wcwy.company.controller;

import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.company.dto.TIndustryDTO;
import com.wcwy.company.entity.TIndustry;
import com.wcwy.company.service.TIndustryService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: TIndustryComtorller
 * Description:
 * date: 2022/9/2 15:01
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "行业信息接口")
@RestController
@RequestMapping("/tIndustry")
public class TIndustryComtorller {
    @Autowired
    private TIndustryService tIndustryService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CompanyMetadata companyMetadata;
    @GetMapping("/selects")
    @ApiOperation(value = "查询行业信息表")
    @Log(title = "查询行业信息表", businessType = BusinessType.SELECT)
    public R<TIndustryDTO> selects() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        List<TIndustryDTO> list1 = (List<TIndustryDTO>) valueOperations.get(RedisCache.TINDUSTRYTYPEDTO.getValue());//reid缓存
        //查看缓存是否存在
        if (list1 !=null && list1.size() > 0) {
            return R.success(list1);
        }

        List<TIndustryDTO> list = tIndustryService.selects();
        if (list.size() > 0) {
            //做缓存
            valueOperations.set(RedisCache.TINDUSTRYTYPEDTO.getValue(), list);
            return R.success(list);
        }
        //防止null攻击
        valueOperations.set(RedisCache.TINDUSTRYTYPEDTO.getValue(), list,10, TimeUnit.SECONDS);
        return R.success(list);
    }

/*    @ApiOperation(value = "添加行业信息")
    public R insert(){

    }*/



}
