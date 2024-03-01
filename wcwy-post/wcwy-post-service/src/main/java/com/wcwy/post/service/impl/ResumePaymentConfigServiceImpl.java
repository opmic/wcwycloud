package com.wcwy.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.post.entity.ResumePaymentConfig;
import com.wcwy.post.service.ResumePaymentConfigService;
import com.wcwy.post.mapper.ResumePaymentConfigMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【resume_payment_config】的数据库操作Service实现
* @createDate 2023-09-07 09:21:32
*/
@Service
public class ResumePaymentConfigServiceImpl extends ServiceImpl<ResumePaymentConfigMapper, ResumePaymentConfig>
    implements ResumePaymentConfigService{

    @Override
    @Cacheable(value="com:resumePaymentConfig:cache:list")
    public List<ResumePaymentConfig> selectList() {
        return this.list();
    }

    @Override
    @Cacheable(value="com:campusRecruitment:cache:one")
    public ResumePaymentConfig campusRecruitment() {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("type",0);
        return this.getOne(queryWrapper);
    }

    @Override
    @Cacheable(value="com:jobMarket:cache:one")
    public ResumePaymentConfig jobMarket() {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("type",1);
        return this.getOne(queryWrapper);
    }
}




