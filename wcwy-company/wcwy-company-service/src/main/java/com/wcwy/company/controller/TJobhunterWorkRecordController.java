package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.entity.TJobhunterHideCompany;
import com.wcwy.company.entity.TJobhunterWorkRecord;
import com.wcwy.company.service.TJobhunterHideCompanyService;
import com.wcwy.company.service.TJobhunterResumeService;
import com.wcwy.company.service.TJobhunterService;
import com.wcwy.company.service.TJobhunterWorkRecordService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.TJobhunterWorkRecordVO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * @author Administrator
 * @description 针对表【t_jobhunter_work_record(工作经历表)】的数据库操作Service
 * @createDate 2022-10-08 11:58:59
 */
@RestController
@RequestMapping("/jobhunterWorkRecord")
@Api(tags = "工作经历接口")
public class TJobhunterWorkRecordController {
    @Autowired
    private TJobhunterWorkRecordService tJobhunterWorkRecordService;
    @Autowired
    private TJobhunterResumeService tJobhunterResumeService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private TJobhunterHideCompanyService tJobhunterHideCompanyService;
    @Autowired
    private IDGenerator idGenerator;

    @PostMapping("/addJobhunterWorkRecord")
    @ApiOperation("添加或修改工作经历")
    @Transactional
    @Log(title = "添加或修改工作经历", businessType = BusinessType.UPDATE)
    public R addJobhunterWorkRecord(@Valid @RequestBody TJobhunterWorkRecordVO jobhunterWorkRecordVO) {
        R jurisdiction = tJobhunterService.jurisdiction(companyMetadata.userid(), jobhunterWorkRecordVO.getUserId());
        if (jurisdiction.getCode() == 405) {
            return jurisdiction;
        }
        TJobhunterWorkRecord tJobhunterWorkRecord = new TJobhunterWorkRecord();
        BeanUtils.copyProperties(jobhunterWorkRecordVO, tJobhunterWorkRecord);
        if (jobhunterWorkRecordVO.getVisible() == 1 && StringUtils.isEmpty(jobhunterWorkRecordVO.getResumeId())) {
            TJobhunterHideCompany tJobhunterHideCompany=new TJobhunterHideCompany();
            tJobhunterHideCompany.setCompanyName(jobhunterWorkRecordVO.getCompanyName());
            tJobhunterHideCompany.setId(idGenerator.generateCode("JH"));
            tJobhunterHideCompany.setCreateTime(LocalDateTime.now());
            tJobhunterHideCompany.setResumeId(companyMetadata.userid());
            boolean save = tJobhunterHideCompanyService.save(tJobhunterHideCompany);
        }else if(! StringUtils.isEmpty(jobhunterWorkRecordVO.getResumeId()) && jobhunterWorkRecordVO.getVisible() == 0) {
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("resume_id",jobhunterWorkRecordVO.getResumeId());
            queryWrapper.eq("company_name",jobhunterWorkRecordVO.getCompanyName());
            tJobhunterHideCompanyService.remove(queryWrapper);
        }
        if (StringUtils.isEmpty(tJobhunterWorkRecord.getWorkId())) {
            tJobhunterWorkRecord.setCreateTime(LocalDateTime.now());
            tJobhunterWorkRecord.setWorkId(idGenerator.generateCode("JW"));
            boolean save = tJobhunterWorkRecordService.save(tJobhunterWorkRecord);
            if (save) {
                return R.success("添加成功");
            } else {
                return R.fail("添加失败");
            }
        }
        tJobhunterWorkRecord.setUpdateTime(LocalDateTime.now());
        boolean b = tJobhunterWorkRecordService.updateById(tJobhunterWorkRecord);
        if (b) {

            return R.success("更新成功！");
        }
        return R.fail("操作失败！");
    }

    @GetMapping("/deleteJobhunterWorkRecord/{workId}/{userId}")
    @ApiOperation("删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "workId", required = true, value = "主键Id")
    })
    @Log(title = "删除", businessType = BusinessType.DELETE)
    public R deleteJobhunterWorkRecord(@PathVariable("workId") String workId, @PathVariable("userId") String userId) {
        R jurisdiction = tJobhunterService.jurisdiction(companyMetadata.userid(), userId);
        Integer code = jurisdiction.getCode();
        if (code == 405) {
            return jurisdiction;
        }
        Integer integer=tJobhunterWorkRecordService.selectCount(userId);
        if(integer==1){
            return R.fail("请保留一份工作经验！");
        }


        boolean b = tJobhunterWorkRecordService.removeById(workId);
        if (b) {
            return R.success("删除成功!");
        }
        return R.fail("删除失败!");
    }

}
