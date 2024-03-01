package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.entity.TJobhunterProjectRecord;
import com.wcwy.company.service.TJobhunterProjectRecordService;
import com.wcwy.company.mapper.TJobhunterProjectRecordMapper;
import com.wcwy.company.vo.AddTJobhunterProjectRecordVO;
import com.wcwy.company.vo.TJobhunterProjectRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
* @author Administrator
* @description 针对表【t_jobhunter_project_record(项目经历表)】的数据库操作Service实现
* @createDate 2022-10-08 11:58:50
*/
@Service
public class TJobhunterProjectRecordServiceImpl extends ServiceImpl<TJobhunterProjectRecordMapper, TJobhunterProjectRecord>
    implements TJobhunterProjectRecordService{
    @Autowired
    private IDGenerator idGenerator;


    @Autowired
    private TJobhunterProjectRecordMapper tJobhunterProjectRecordMapper;

    @Override
    public Boolean adds(List<AddTJobhunterProjectRecordVO> tJobhunterProjectRecordVO, String s) {
        List<TJobhunterProjectRecord> list=new ArrayList<>();
        for (AddTJobhunterProjectRecordVO jobhunterProjectRecordVO : tJobhunterProjectRecordVO) {
            TJobhunterProjectRecord tJobhunterProjectRecord=new TJobhunterProjectRecord();
            BeanUtils.copyProperties(jobhunterProjectRecordVO,tJobhunterProjectRecord);
            tJobhunterProjectRecord.setCreateTime(LocalDateTime.now());
            tJobhunterProjectRecord.setProjectId(idGenerator.generateCode("PR"));
            tJobhunterProjectRecord.setResumeId(s);
            list.add(tJobhunterProjectRecord);
        }
        return this.saveBatch(list);
    }

    @Override
    public Integer selectCount(String userId) {
        return tJobhunterProjectRecordMapper.selectCountId(userId);
    }
}




