package com.wcwy.post.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.*;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.post.dto.TotalPostShare;
import com.wcwy.post.entity.CompanyUserRole;
import com.wcwy.post.entity.JobHunterGlanceOver;
import com.wcwy.post.entity.TPostShare;
import com.wcwy.post.produce.GoldProduce;
import com.wcwy.post.produce.PutInResumeProduce;
import com.wcwy.post.produce.TestProduce;
import com.wcwy.post.service.CompanyUserRoleService;
import com.wcwy.post.service.JobHunterGlanceOverService;
import com.wcwy.post.service.TCompanyPostService;
import com.wcwy.post.service.TPostShareService;
import com.wcwy.post.session.CompanyMetadata;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description 针对表【t_post_share(发布岗位纪录表)】的数据库操作Service
 * @createDate 2022-09-15 17:26:24
 */
@RestController
@Api(tags = "发布岗位纪录接口")
@RequestMapping("/tPostShare")
@Slf4j
public class TPostShareController {
    @Resource
    private TPostShareService tPostShareService;
    @Autowired
    TCompanyPostService tCompanyPostService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private JobHunterGlanceOverService jobHunterGlanceOverService;
    @Autowired
    private GoldProduce goldProduce;
    @Autowired
    TestProduce testProduce;
    @Autowired
    private IDGenerator idGenerator;
    @GetMapping("/select")
    public TPostShare select(@RequestParam("companyPostId") String companyPostId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("company_post_id", companyPostId);
        TPostShare one = tPostShareService.getOne(queryWrapper);
        return one;
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PutInResumeProduce putInResumeProduce;
    @Autowired
    private CompanyUserRoleService companyUserRoleService;

/*    @GetMapping("/test")
    @ApiOperation(value = "测试")
    public String test() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("company_user_role_id", "CU2212080947367-6");
        List<CompanyUserRole> list = companyUserRoleService.list(queryWrapper);
        if (list.size() > 0) {
            for (CompanyUserRole companyUserRole : list) {
                Map map = new HashMap();
                map.put("company_id", companyUserRole.getCompanyId());
                map.put("user_id", companyUserRole.getUserId());
                map.put("put_in_resume_id", companyUserRole.getPutInResumeId());
                String toJSONString = JSON.toJSONString(map);
                putInResumeProduce.sendOrderlyMessage(toJSONString);
                // putInResumeApi.updateDownloadResume(companyUserRole.getCompanyId(),companyUserRole.getUserId(),companyUserRole.getPutInResumeId());
            }
        }
        return "发送成功";
    }*/

    @GetMapping("/augmentFlow")
    @ApiOperation("添加热度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", required = true, value = "岗位id"),
            @ApiImplicitParam(name = "userid", required = false, value = "浏览者id")
    })
    @Log(title = "添加热度", businessType = BusinessType.UPDATE)
    public R augmentFlow(@RequestParam("postId") String postId,@RequestParam(required = false,value = "userid") String userid) {
        if (StringUtils.isEmpty(postId)) {
            return R.fail("传入值不能为空!");
        }
        if(! StringUtils.isEmpty(userid)){
            String substring = userid.substring(0, 2);
            if("TJ".equals(substring)){
                Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(Lock.UPDATE_HOT.getLock() + userid + postId, "1", 10, TimeUnit.SECONDS);
                if(aBoolean){
                    QueryWrapper queryWrapper=new QueryWrapper();
                    queryWrapper.eq("jobhunter_id",userid);
                    queryWrapper.eq("glance_over_post",postId);
                    JobHunterGlanceOver one = jobHunterGlanceOverService.getOne(queryWrapper);
                    if(one !=null){
                        one.setGlanceOverTime(LocalDateTime.now());
                        jobHunterGlanceOverService.updateById(one);
                    }else {
                        JobHunterGlanceOver jobHunterGlanceOver=new JobHunterGlanceOver();
                        jobHunterGlanceOver.setGlanceOverTime(LocalDateTime.now());
                        jobHunterGlanceOver.setDeleted(0);
                        jobHunterGlanceOver.setGlanceOverPost(postId);
                        jobHunterGlanceOver.setJobhunterId(userid);
                        jobHunterGlanceOver.setGlanceOverId(idGenerator.generateCode("HG"));
                        jobHunterGlanceOverService.save(jobHunterGlanceOver);
                    }

                }
            }
        }
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        Long increment = valueOperations.increment(PostRecordSole.FLOW.getValue() + postId);
        if(increment==1){
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("company_post_id", postId);
            TPostShare one = tPostShareService.getOne(queryWrapper);
            valueOperations.set(PostRecordSole.FLOW.getValue() + postId,one.getFlow());
            increment =one.getFlow()+1L;
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_post_id", postId);
        updateWrapper.set("flow",increment);
        boolean update = tPostShareService.update(updateWrapper);
        if(update){
            return R.success();
        }
        return R.fail();
    }

    @GetMapping("/selectTotalPostShare")
    @ApiOperation("获取招聘公司的招聘数据")
    public TotalPostShare selectTotalPostShare(@RequestParam("companyId") String companyId){
        TotalPostShare totalPostShare=new TotalPostShare();
        totalPostShare.setShareSize(0L);
        totalPostShare.setFlow(0L);
        totalPostShare.setDownloadSize(0L);
        totalPostShare.setBrowseSize(0L);
        totalPostShare.setInterviewSize(0L);
        totalPostShare.setEntrySize(0L);
        totalPostShare.setWeedOut(0L);
        totalPostShare.setSubscribe(0L);
        totalPostShare.setOfferSize(0L);
        TotalPostShare totalPostShare1=  tPostShareService.selectTotalPostShare(companyId);
        if(totalPostShare1 !=null){
            BeanUtils.copyProperties(totalPostShare1,totalPostShare);
        }
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("company_id",companyId);
        queryWrapper.eq("status",1);
        int count = tCompanyPostService.count(queryWrapper);
        totalPostShare.setOnLinePost((long) count);
        return totalPostShare;
    }


}
