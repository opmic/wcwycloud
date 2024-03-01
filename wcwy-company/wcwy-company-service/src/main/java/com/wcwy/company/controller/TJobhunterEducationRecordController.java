package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.EducationUtil;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.dto.JobHunterEducationRecordDTO;
import com.wcwy.company.entity.TJobhunter;
import com.wcwy.company.entity.TJobhunterEducationRecord;
import com.wcwy.company.service.TJobhunterEducationRecordService;
import com.wcwy.company.service.TJobhunterResumeService;
import com.wcwy.company.service.TJobhunterService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.TJobhunterEducationRecordVO;
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
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【t_jobhunter_education_record(教育经历表)】的数据库操作Service
 * @createDate 2022-10-08 11:58:38
 */
@RestController
@RequestMapping("/jobhunterEducationRecord")
@Api(tags = "教育经历接口")
public class TJobhunterEducationRecordController {
    @Autowired
    private TJobhunterEducationRecordService tJobhunterEducationRecordService;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private TJobhunterResumeService tJobhunterResumeService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @PostMapping("/insertJobhunterEducationRecord")
    @ApiOperation("添加+修改教育经历")
    @Log(title = "添加+修改教育经历", businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    public R insertJobhunterEducationRecord(@Valid @RequestBody TJobhunterEducationRecordVO tJobhunterEducationRecordVO) {
        R jurisdiction = tJobhunterService.jurisdiction(companyMetadata.userid(), tJobhunterEducationRecordVO.getUserId());
        if(jurisdiction.getCode()==405){
            return jurisdiction;
        }
        TJobhunterEducationRecord tJobhunterEducationRecord = new TJobhunterEducationRecord();
        //更新求职者的学历
        TJobhunter byId = tJobhunterService.getById(tJobhunterEducationRecordVO.getUserId());
        if(byId ==null){
            return R.fail("求职者不存在!");
        }

        BeanUtils.copyProperties(tJobhunterEducationRecordVO,tJobhunterEducationRecord);
        if (StringUtils.isEmpty(tJobhunterEducationRecordVO.getEduId())) {

            Integer integer= tJobhunterEducationRecordService.selectCount(tJobhunterEducationRecordVO.getUserId());
            if(integer>=3){
                return R.fail("请限制在三份以内！");
            }
            tJobhunterEducationRecord.setCreateTime(LocalDateTime.now());
            tJobhunterEducationRecord.setEduId(idGenerator.generateCode("ER"));
            boolean save = tJobhunterEducationRecordService.save(tJobhunterEducationRecord);
            if (save) {
                if(Integer.parseInt(byId.getEducation())<Integer.parseInt(tJobhunterEducationRecordVO.getEducation())){
                    UpdateWrapper updateWrapper=new UpdateWrapper();
                    updateWrapper.eq("user_id",tJobhunterEducationRecordVO.getUserId());
                    updateWrapper.set("education",tJobhunterEducationRecordVO.getEducation());
                    tJobhunterService.update(updateWrapper);
                }
                return R.success("添加成功！");
            }else {

                return R.fail("添加失败！");
            }
        }
        tJobhunterEducationRecord.setUpdateTime(LocalDateTime.now());
        boolean b = tJobhunterEducationRecordService.updateById(tJobhunterEducationRecord);
        if (b) {
            if(Integer.parseInt(byId.getEducation())<Integer.parseInt(tJobhunterEducationRecordVO.getEducation())){
                UpdateWrapper updateWrapper=new UpdateWrapper();
                updateWrapper.eq("user_id",tJobhunterEducationRecordVO.getUserId());
                updateWrapper.set("education",tJobhunterEducationRecordVO.getEducation());
                tJobhunterService.update(updateWrapper);
            }
            return R.success("更新成功！");
        }else {
            return R.fail("更新失败！");
        }
    }

    @GetMapping("delete")
    @ApiOperation("删除教育经历")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "eduId", required = true, value = "主键Id")
    })
    @Log(title = "删除教育经历", businessType = BusinessType.UPDATE)
    public R delete(@RequestParam("userId") String userId ,@RequestParam("eduId") String eduId ){
        R jurisdiction = tJobhunterService.jurisdiction(companyMetadata.userid(), userId);
        Integer code = jurisdiction.getCode();
        if(code==405){
            return jurisdiction;
        }
       Integer integer= tJobhunterEducationRecordService.selectCount(userId);
        if(integer<=1){
            return R.fail("请保留最后一份!");
        }
        boolean b = tJobhunterEducationRecordService.removeById(eduId);
        if(b){
            return R.success("删除成功!");
        }
        return R.fail("删除失败!");
    }

    @GetMapping("/test")
    @ApiOperation("矫正接口")
    public void test(){
        List<JobHunterEducationRecordDTO> jobHunterEducationRecordDTO=  tJobhunterEducationRecordService.correct();
        if(jobHunterEducationRecordDTO.size()>0){
            for (JobHunterEducationRecordDTO hunterEducationRecordDTO : jobHunterEducationRecordDTO) {
                TJobhunter byId = tJobhunterService.getById(hunterEducationRecordDTO.getJobHunterId());
                if(byId ==null){
                    continue;
                }
                if(Integer.parseInt(byId.getEducation())<Integer.parseInt(hunterEducationRecordDTO.getEducation())){
                    UpdateWrapper updateWrapper=new UpdateWrapper();
                    updateWrapper.eq("user_id",hunterEducationRecordDTO.getJobHunterId());
                    updateWrapper.set("education",hunterEducationRecordDTO.getEducation());
                    tJobhunterService.update(updateWrapper);
                }
            }
        }

    }
}
