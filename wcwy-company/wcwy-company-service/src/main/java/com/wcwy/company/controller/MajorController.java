package com.wcwy.company.controller;

import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.company.dto.MajorParentDTO;
import com.wcwy.company.service.MajorService;
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
 * ClassName: MajorController
 * Description:
 * date: 2022/9/7 15:03
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "专业字典表接口")
@RequestMapping("/major")
@RestController
public class MajorController {
    @Autowired
    private MajorService majorService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/selectList")
    @ApiOperation(value = "查询专业")
    @Log(title = "查询专业", businessType = BusinessType.SELECT)
    public R<MajorParentDTO> selectList(){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        List<MajorParentDTO> list1  = (List<MajorParentDTO>) valueOperations.get(RedisCache.MAHORPARENTDTO.getValue());//reid缓存
        if(list1 !=null && list1.size()>0){
            return R.success(list1);
        }
     List<MajorParentDTO> list= majorService.selectList();
        if(list !=null && list.size()>0){
            valueOperations.set(RedisCache.MAHORPARENTDTO.getValue(),list);
        }
     return R.success(list);
    }
}
