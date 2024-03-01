package com.wcwy.company.asyn;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.redis.enums.QRCode;
import com.wcwy.company.entity.ReferrerRecord;
import com.wcwy.company.service.ReferrerRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * ClassName: ReferrerRecordAsync
 * Description:
 * date: 2023/7/15 10:52
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class  ReferrerRecordAsync {
    @Autowired
   private ReferrerRecordService referrerRecordService;

    /**
     * putInResume：投简人
     * jobHunter:求职者
     * @param putInResume
     * @param jobHunter
     */
    @Async
    public void browse(String putInResume,String jobHunter){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id",jobHunter);
        queryWrapper.eq("recommend_id",putInResume);
        ReferrerRecord r = referrerRecordService.getOne(queryWrapper);
        if(r ==null){
            return;
        }
        r.setBrowse(r.getBrowse()+1L);
        referrerRecordService.updateById(r);
    }

    @Async
    public void appoint(String putInResume,String jobHunter){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id",jobHunter);
        queryWrapper.eq("recommend_id",putInResume);
        ReferrerRecord r = referrerRecordService.getOne(queryWrapper);
        if(r ==null){
            return;
        }
        r.setAppoint(r.getAppoint()+1L);
        referrerRecordService.updateById(r);
    }
    @Async
    public void interview(String putInResume,String jobHunter){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id",jobHunter);
        queryWrapper.eq("recommend_id",putInResume);
        ReferrerRecord r = referrerRecordService.getOne(queryWrapper);
        if(r ==null){
            return;
        }
        r.setInterview(r.getInterview()+1L);
        referrerRecordService.updateById(r);
    }
    @Async
    public void offer(String putInResume,String jobHunter){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id",jobHunter);
        queryWrapper.eq("recommend_id",putInResume);
        ReferrerRecord r = referrerRecordService.getOne(queryWrapper);
        if(r ==null){
            return;
        }
        r.setOffer(r.getOffer()+1L);
        referrerRecordService.updateById(r);
    }

    @Async
    public void entry(String putInResume,String jobHunter){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id",jobHunter);
        queryWrapper.eq("recommend_id",putInResume);
        ReferrerRecord r = referrerRecordService.getOne(queryWrapper);
        if(r ==null){
            return;
        }
        r.setEntry(r.getEntry()+1L);
        referrerRecordService.updateById(r);
    }

    public void weedOut(String putInResume,String jobHunter) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id",jobHunter);
        queryWrapper.eq("recommend_id",putInResume);
        ReferrerRecord r = referrerRecordService.getOne(queryWrapper);
        if(r ==null){
            return;
        }
        r.setWeedOut(r.getWeedOut()+1L);
        referrerRecordService.updateById(r);
    }
}
