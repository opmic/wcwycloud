package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.dto.JobHunterEducationRecordDTO;
import com.wcwy.company.entity.TJobhunterEducationRecord;
import com.wcwy.company.service.TJobhunterEducationRecordService;
import com.wcwy.company.mapper.TJobhunterEducationRecordMapper;
import com.wcwy.company.service.TJobhunterService;
import com.wcwy.company.vo.AddTJobhunterEducationRecordVO;
import com.wcwy.company.vo.TJobhunterEducationRecordVO;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
* @author Administrator
* @description 针对表【t_jobhunter_education_record(教育经历表)】的数据库操作Service实现
* @createDate 2022-10-08 11:58:38
*/
@Service
public class TJobhunterEducationRecordServiceImpl extends ServiceImpl<TJobhunterEducationRecordMapper, TJobhunterEducationRecord>
    implements TJobhunterEducationRecordService{
    @Autowired
    private TJobhunterEducationRecordMapper tJobhunterEducationRecordMapper;

    @Autowired
    private IDGenerator idGenerator;

    @Autowired
    private TJobhunterService tJobhunterService;
    @Override
    public String getEduId(String userid) {
        return tJobhunterEducationRecordMapper.getEduId(userid);
    }

    @Override
    public Integer selectCount(String userId) {
        return tJobhunterEducationRecordMapper.count(userId);
    }

    @Override
    public List<JobHunterEducationRecordDTO> correct() {
        return tJobhunterEducationRecordMapper.correct();
    }

    @Override
    public Boolean adds(List<AddTJobhunterEducationRecordVO> tJobhunterEducationRecordVO, String s,String userid) throws Exception {
        List<TJobhunterEducationRecord> list=new ArrayList(tJobhunterEducationRecordVO.size());
        Integer education =0;
        for (AddTJobhunterEducationRecordVO jobhunterEducationRecordVO : tJobhunterEducationRecordVO) {
            TJobhunterEducationRecord tJobhunterEducationRecord=new TJobhunterEducationRecord();
            BeanUtils.copyProperties(jobhunterEducationRecordVO,tJobhunterEducationRecord);
            tJobhunterEducationRecord.setCreateTime(LocalDateTime.now());
            tJobhunterEducationRecord.setResumeId(s);
            tJobhunterEducationRecord.setEduId(idGenerator.generateCode("ER"));
            try {
                Integer.parseInt(jobhunterEducationRecordVO.getEducation());
            }catch (Exception e){
                throw new Exception("学历传入数字！");
            }
            if(Integer.parseInt(jobhunterEducationRecordVO.getEducation())>education){
                education=Integer.parseInt(jobhunterEducationRecordVO.getEducation());
            }
            list.add(tJobhunterEducationRecord);
        }
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("user_id",userid);
        updateWrapper.set("education",education);
        tJobhunterService.update(updateWrapper);
        return this.saveBatch(list);
    }
}




