package com.wcwy.company.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.entity.TJobhunterProjectRecord;
import com.wcwy.company.service.TJobhunterProjectRecordService;
import com.wcwy.company.service.TJobhunterResumeService;
import com.wcwy.company.service.TJobhunterService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.TJobhunterProjectRecordAppVO;
import com.wcwy.company.vo.TJobhunterProjectRecordVO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
* @author Administrator
* @description 针对表【t_jobhunter_project_record(项目经历表)】的数据库操作Service
* @createDate 2022-10-08 11:58:50
*/
@RestController
@RequestMapping("/jobhunterProjectRecord")
@Api(tags = "项目经历接口")
public class TJobhunterProjectRecordCntroller {
    @Autowired
   private TJobhunterProjectRecordService tJobhunterProjectRecordService;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private TJobhunterResumeService tJobhunterResumeService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private TJobhunterService tJobhunterService;
    @PostMapping("/insertJobhunterProjectRecord")
    @ApiOperation("添加+修改项目经历接口")
    @Log(title = "添加+修改项目经历接口", businessType = BusinessType.INSERT)
    public R insertJobhunterProjectRecord(@Valid @RequestBody TJobhunterProjectRecordVO tJobhunterProjectRecordVO){
        R jurisdiction = tJobhunterService.jurisdiction(companyMetadata.userid(), tJobhunterProjectRecordVO.getUserId());
        if(jurisdiction.getCode()==405){
            return jurisdiction;
        }
        TJobhunterProjectRecord tJobhunterProjectRecord=new TJobhunterProjectRecord();
        BeanUtils.copyProperties(tJobhunterProjectRecordVO,tJobhunterProjectRecord);
        if(StringUtils.isEmpty(tJobhunterProjectRecordVO.getProjectId())){//添加
            tJobhunterProjectRecord.setCreateTime(LocalDateTime.now());
            tJobhunterProjectRecord.setProjectId(idGenerator.generateCode("PR"));
            boolean save = tJobhunterProjectRecordService.save(tJobhunterProjectRecord);
            if(save){
                return R.success("添加成功！");
            }else{
                return R.fail("添加失败！");
            }
        }
        tJobhunterProjectRecord.setUpdateTime(LocalDateTime.now());
        boolean update = tJobhunterProjectRecordService.updateById(tJobhunterProjectRecord);
        if(update){
            return R.success("更新成功！");
        }else{
            return R.fail("更新失败！");
        }
    }

    @PostMapping("/deleteJobhunterProjectRecord/{projectId}/{userId}")
    @ApiOperation("删除目经历")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "projectId", required = true, value = "主键Id")
    })
    @Log(title = "删除目经历", businessType = BusinessType.DELETE)
    public R deleteJobhunterProjectRecord(@PathVariable("projectId") String projectId,@PathVariable("userId") String userId){
        R jurisdiction = tJobhunterService.jurisdiction(companyMetadata.userid(), userId);
        if(jurisdiction.getCode()==405){
            return jurisdiction;
        }
        Integer integer=   tJobhunterProjectRecordService.selectCount(userId);
        if(integer==1){
            return R.fail("请保留一份项目经历！");
        }
        boolean b = tJobhunterProjectRecordService.removeById(projectId);
        if(b){
           return R.success("删除成功!");
        }
        return R.fail("删除失败!");
    }


    @PostMapping("/insertAppJobhunterProjectRecord")
    @ApiOperation("APP+添加+修改项目经历接口")
    @Log(title = "APP+添加+修改项目经历接口", businessType = BusinessType.INSERT)
    public R insertAppJobhunterProjectRecord(@Valid @RequestBody TJobhunterProjectRecordAppVO tJobhunterProjectRecordVO){
        R jurisdiction = tJobhunterService.jurisdiction(companyMetadata.userid(), tJobhunterProjectRecordVO.getUserId());
        if(jurisdiction.getCode()==405){
            return jurisdiction;
        }
        TJobhunterProjectRecord tJobhunterProjectRecord=new TJobhunterProjectRecord();
        BeanUtils.copyProperties(tJobhunterProjectRecordVO,tJobhunterProjectRecord);
        if(StringUtils.isEmpty(tJobhunterProjectRecordVO.getProjectId())){//添加
            tJobhunterProjectRecord.setCreateTime(LocalDateTime.now());
            tJobhunterProjectRecord.setProjectId(idGenerator.generateCode("PR"));
            boolean save = tJobhunterProjectRecordService.save(tJobhunterProjectRecord);
            if(save){
                return R.success("添加成功！");
            }else{
                return R.fail("添加失败！");
            }
        }
        tJobhunterProjectRecord.setUpdateTime(LocalDateTime.now());
        boolean update = tJobhunterProjectRecordService.updateById(tJobhunterProjectRecord);
        if(update){
            return R.success("更新成功！");
        }else{
            return R.fail("更新失败！");
        }
    }
}
