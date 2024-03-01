package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.entity.CooCollect;
import com.wcwy.company.service.CooCollectService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: CooCollectController
 * Description:
 * date: 2024/1/19 16:03
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/CooCollect")
@Api(tags = "coo部落发帖收藏")
public class CooCollectController {

    @Autowired
    private CompanyMetadata companyMetadata;

    @Autowired
    private CooCollectService cooCollectService;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/updateCollect")
    @ApiOperation("收藏/收藏收藏")
    @Log(title = "收藏/收藏收藏", businessType = BusinessType.UPDATE)
    @ApiImplicitParam(name = "id", required = true, value = "帖子id")
    public R updateCollect(@RequestParam("id") Long id){
        LambdaQueryWrapper<CooCollect> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(CooCollect::getCooTribe,id );
        lambdaQueryWrapper.eq(CooCollect::getCreateUser,companyMetadata.userid());
        int count = cooCollectService.count(lambdaQueryWrapper);
        if(count==0){
            CooCollect cooCollect=new CooCollect();
            cooCollect.setCooTribe(id);
            cooCollect.setCreateUser(companyMetadata.userid());
            cooCollectService.save(cooCollect);
            redisUtils.incr(Cache.CACHE_COLLECT_TRIBE.getKey()+id,1);
            redisUtils.sSet(Cache.CACHE_RECORD_COLLECT_TRIBE.getKey(),id);
        }else {
            cooCollectService.remove(lambdaQueryWrapper);
            redisUtils.incr1(Cache.CACHE_COLLECT_TRIBE.getKey()+id,-1);
        }
        return R.success();
    }


}
