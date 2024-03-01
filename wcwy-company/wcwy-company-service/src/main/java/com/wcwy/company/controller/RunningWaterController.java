package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.result.R;
import com.wcwy.company.entity.Major;
import com.wcwy.company.entity.RunningWater;
import com.wcwy.company.query.RunningWaterQuery;
import com.wcwy.company.service.RunningWaterService;
import com.wcwy.company.session.CompanyMetadata;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * ClassName: RunningWaterController
 * Description:
 * date: 2023/6/30 14:41
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "无忧币和金币流水账接口")
@RestController
@RequestMapping("/runningWater")
public class RunningWaterController {
    @Autowired
    private RunningWaterService runningWaterService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @PostMapping("/select")
    @ApiOperation("查询")
    public R<RunningWater> select(@RequestBody RunningWaterQuery runningWaterQuery){

        QueryWrapper queryWrapper=new QueryWrapper();
       if(runningWaterQuery.getType() !=null){
           queryWrapper.eq("type",runningWaterQuery.getType());
       }
       if(runningWaterQuery.getStartDate() !=null &&  runningWaterQuery.getEndDate() ==null){
           queryWrapper.ge("crate_time",runningWaterQuery.getStartDate());
       }
        if(runningWaterQuery.getStartDate() ==null &&  runningWaterQuery.getEndDate() !=null){
            queryWrapper.le("crate_time",runningWaterQuery.getEndDate());
        }

        if(runningWaterQuery.getIfIncome() !=null){
            queryWrapper.eq("if_income",runningWaterQuery.getIfIncome());
        }


        if(runningWaterQuery.getStartDate() !=null &&  runningWaterQuery.getEndDate() !=null){
            queryWrapper.between("crate_time",runningWaterQuery.getStartDate(), runningWaterQuery.getEndDate());
        }
        queryWrapper.orderByDesc("crate_time");
        queryWrapper.eq("user_id",companyMetadata.userid());
         IPage page = runningWaterService.page(runningWaterQuery.createPage(), queryWrapper);
        return R.success(page);

    }


}
