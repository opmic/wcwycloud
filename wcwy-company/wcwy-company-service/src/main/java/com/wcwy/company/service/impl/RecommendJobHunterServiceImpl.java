package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.entity.RecommendJobHunter;
import com.wcwy.company.service.RecommendJobHunterService;
import com.wcwy.company.mapper.RecommendJobHunterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author Administrator
* @description 针对表【recommend_job_hunter(推荐官求职者权限表)】的数据库操作Service实现
* @createDate 2023-07-24 09:21:49
*/
@Service
public class RecommendJobHunterServiceImpl extends ServiceImpl<RecommendJobHunterMapper, RecommendJobHunter>
    implements RecommendJobHunterService{

    @Autowired
    private IDGenerator idGenerator;
    @Override
    public void add(String recommend,String tJobHunterId) {
        RecommendJobHunter recommendJobHunter=new RecommendJobHunter();
        recommendJobHunter.setRoleId(idGenerator.generateCode("RJH"));
        recommendJobHunter.setRecommendId(recommend);
        recommendJobHunter.setJobHunterId(tJobHunterId);
        recommendJobHunter.setCreateTime(LocalDateTime.now());
        recommendJobHunter.setDeleted(0);
        this.save(recommendJobHunter);
    }

    @Override
    public RecommendJobHunter selectById(String userid, String jobHunterId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("recommend_id",userid);
        queryWrapper.eq("job_hunter_id",jobHunterId);
        queryWrapper.eq("deleted",0);
        RecommendJobHunter one = this.getOne(queryWrapper);
        return one;
    }
}




