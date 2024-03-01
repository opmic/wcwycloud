package com.wcwy.company.controller;

import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.company.entity.Education;
import com.wcwy.company.service.EducationService;
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

/**
 * ClassName: EducationController
 * Description:
 * date: 2022/9/7 14:09
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/education")
@Api(tags = "学历信息接口")
public class EducationController {

    @Autowired
    private EducationService educationService;
    @Autowired
    private RedisTemplate redisTemplate;
    @ApiOperation(value = "查询学历字典表", notes = "查询学历字典表")
    @GetMapping("selectList")
    @Log(title = "查询学历字典表", businessType = BusinessType.SELECT)
    public R<Education> selectList() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        List<Education> list1  = (List<Education>) valueOperations.get(RedisCache.EDUCATION.getValue());//reid缓存
        if(list1 !=null && list1.size()>0){
            return R.success(list1);
        }
        List<Education> list = educationService.list();
        if(list.size()>0){
            valueOperations.set(RedisCache.EDUCATION.getValue(),list);
        }
        return R.success(list);
    }

}
