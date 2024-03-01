package com.wcwy.company.controller;

import com.wcwy.common.base.result.R;
import com.wcwy.company.entity.ParticipateInActivities;
import com.wcwy.company.service.ParticipateInActivitiesService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * ClassName: ParticipateInActivitiesController
 * Description:
 * date: 2023/11/13 13:58
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "参加活动接口")
@RequestMapping("/participateInActivities")
@RestController
public class ParticipateInActivitiesController {


    @Autowired
    private ParticipateInActivitiesService participateInActivitiesService;

    @Autowired
    private CompanyMetadata companyMetadata;

    @GetMapping("/add")
    @ApiOperation("参加活动")
    @Log(title = "参加活动", businessType = BusinessType.INSERT)
    public R add(){
        String userid = companyMetadata.userid();
        String substring = userid.substring(0,2);
        if(! "TR".equals(substring)){
            return R.fail("请使用推荐官账号！");
        }
        ParticipateInActivities byId = participateInActivitiesService.getById(userid);
        if(byId !=null){
            return R.fail("您已参加！");
        }
        ParticipateInActivities participateInActivities=new ParticipateInActivities();
        participateInActivities.setRecommendId(userid);
        participateInActivities.setCreateTime(LocalDateTime.now());
        boolean b = participateInActivitiesService.saveOrUpdate(participateInActivities);
        if(b){
            return R.success("参加成功！");
        }
        return R.fail("操作失败!");
    }

    @GetMapping("select")
    @ApiOperation("查询参加活动")
    @Log(title = "查询参加活动", businessType = BusinessType.INSERT)
    public R select(){
      return R.success( participateInActivitiesService.getById(companyMetadata.userid()));
    }
}
