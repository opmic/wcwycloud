package com.wcwy.company.controller;

import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.dto.TProvincesCitieDTO;
import com.wcwy.company.entity.TCities;
import com.wcwy.company.entity.TProvinces;
import com.wcwy.company.service.TCitiesService;
import com.wcwy.company.service.TProvincesService;
import com.wcwy.company.service.TRecommendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * ClassName: TProvincesController
 * Descriptionccc
 * date: 2022/9/2 10:50
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "省市接口")
@RestController
@RequestMapping("/tProvinces")
public class TProvincesController {
    @Autowired
   private TProvincesService tProvincesService;
    @Autowired
    private TCitiesService tCitiesService;
    @Autowired
    private TRecommendService tRecommendService;
    @Autowired
   private RedisUtils redisUtils;
    @GetMapping("/selectPC")
    public R<TProvincesCitieDTO> selectPC(){
        List<TProvincesCitieDTO> tProvincesCitieDTOS = (List<TProvincesCitieDTO>) redisUtils.get(RedisCache.TPRVINCESCITIEDTO.getKey());
        if(tProvincesCitieDTOS !=null && tProvincesCitieDTOS.size()>0){
            return R.success(tProvincesCitieDTOS);
        }

        List<TProvincesCitieDTO> list= tProvincesService.selectPC();
        if(list.size() ==0){
            redisUtils.set(RedisCache.TPRVINCESCITIEDTO.getKey(),list,60*5);
        }
        redisUtils.set(RedisCache.TPRVINCESCITIEDTO.getValue(),list);
       return R.success(list);
    }

    @GetMapping("/createCache")
    @ApiOperation("创建")
    public void createCache(){
        this.cache();
    }

    @PostConstruct
    public void cache(){
        List<TProvinces> list = tProvincesService.list();
        String key = Cache.CACHE_CITY.getKey();
        redisUtils.del(key);
        for (TProvinces tProvinces : list) {
            redisUtils.set(key+tProvinces.getProvince(),tProvinces.getProvinceid());
        }
        List<TCities> list1 = tCitiesService.list();
        for (TCities tCities : list1) {
            redisUtils.set(key+tCities.getCity(),tCities.getCityid());
        }
    }

}
