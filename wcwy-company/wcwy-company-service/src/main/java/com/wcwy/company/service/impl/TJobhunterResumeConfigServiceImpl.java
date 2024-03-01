package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.company.entity.TJobhunterResumeConfig;
import com.wcwy.company.service.TJobhunterResumeConfigService;
import com.wcwy.company.mapper.TJobhunterResumeConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author Administrator
* @description 针对表【t_jobhunter_resume_config(隐藏简历配置表)】的数据库操作Service实现
* @createDate 2022-10-08 11:58:56
*/
@Service
@Slf4j
public class TJobhunterResumeConfigServiceImpl extends ServiceImpl<TJobhunterResumeConfigMapper, TJobhunterResumeConfig>
    implements TJobhunterResumeConfigService{
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public  void  updateRedis(String userId){

        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        TJobhunterResumeConfig byId = this.getOne(queryWrapper);
        if(byId ==null){
            //如果不存在创建
            TJobhunterResumeConfig tJobhunterResumeConfig=new TJobhunterResumeConfig();
            tJobhunterResumeConfig.setUserId(userId);
            tJobhunterResumeConfig.setVisible(0);
            tJobhunterResumeConfig.setHunterVisible(0);
            tJobhunterResumeConfig.setHunterPositionVisible(0);
            tJobhunterResumeConfig.setCreateTime(LocalDateTime.now());
            boolean save = this.save(tJobhunterResumeConfig);
            byId = this.getOne(queryWrapper);
        }
        ValueOperations<String, Object> value = redisTemplate.opsForValue();

        redisTemplate.delete(RedisCache.TJOBHUNTERRESUMECONFIG.getValue()+ ":" + byId.getUserId());
        value.set(RedisCache.TJOBHUNTERRESUMECONFIG.getValue() + ":" + byId.getUserId(), byId);
    }
}




