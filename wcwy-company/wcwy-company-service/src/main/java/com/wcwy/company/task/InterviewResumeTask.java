package com.wcwy.company.task;

import cn.hutool.core.util.PhoneUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Message;
import com.wcwy.company.entity.InterviewResume;
import com.wcwy.company.entity.PutInResume;
import com.wcwy.company.service.InterviewResumeService;
import com.wcwy.company.service.PutInResumeService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.AuditInterviewResume;
import com.wcwy.company.vo.InterviewResumeVO;
import com.wcwy.company.vo.SuggestInterviewResume;
import com.wcwy.company.vo.UpdateInterviewResume;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Administrator
 * @description 针对表【interview_resume(面试邀请表)】的数据库操作Service
 * @createDate 2022-10-27 11:44:12
 */
@Slf4j
@Component
public class InterviewResumeTask {
    @Autowired
    private InterviewResumeService interviewResumeService;
    @Resource
    private PutInResumeService putInResumeService;

  //  @Scheduled(cron = "0 */1 * * * ?")//每天1分钟执行一次
    public void interview(){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("subscribe_if",2);
        queryWrapper. le("subscribe_time",LocalDateTime.now());
        queryWrapper. eq("resume_state",4);
        List<PutInResume> list = putInResumeService.list(queryWrapper);
        for (PutInResume interviewResume : list) {
            UpdateWrapper updateWrapper=new UpdateWrapper();
            updateWrapper.eq("put_in_resume_id",interviewResume.getPutInResumeId());
            updateWrapper.set("resume_state",5);
            updateWrapper.set("interview_if",2);
            updateWrapper.set("interview_time",LocalDateTime.now());
            putInResumeService.update(updateWrapper);
            updateWrapper=null;
        }
    }

    //面试时间过了还未参加面试则自动取消
    @Scheduled(cron = "0 */10 * * * ?")//每天10分钟执行一次
    public void cancel(){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("take",1);
        queryWrapper.le("interview_time",LocalDateTime.now());
        List<InterviewResume> list = interviewResumeService.list(queryWrapper);
        for (InterviewResume interviewResume : list) {
            interviewResume.setCompletionStatus(1);
            interviewResume.setStage(4);
            interviewResume.setTake(3);
            interviewResume.setNoTakeCause("面试时间已过");
            interviewResume.setStateCause("该面试已过期未处理");
            interviewResumeService.updateById(interviewResume);
        }
    }
}