package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.entity.TJobhunterWorkRecord;
import com.wcwy.company.service.TJobhunterWorkRecordService;
import com.wcwy.company.mapper.TJobhunterWorkRecordMapper;
import com.wcwy.company.vo.AddTJobhunterWorkRecordVO;
import com.wcwy.company.vo.TJobhunterWorkRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
* @author Administrator
* @description 针对表【t_jobhunter_work_record(工作经历表)】的数据库操作Service实现
* @createDate 2022-10-08 11:58:59
*/
@Service
public class TJobhunterWorkRecordServiceImpl extends ServiceImpl<TJobhunterWorkRecordMapper, TJobhunterWorkRecord>
    implements TJobhunterWorkRecordService{
    @Autowired
    private IDGenerator idGenerator;

    @Resource
    private TJobhunterWorkRecordMapper tJobhunterWorkRecordMapper;

    @Override
    public Boolean adds(List<AddTJobhunterWorkRecordVO> jobhunterWorkRecordVO, String s) {
        List<TJobhunterWorkRecord> list=new ArrayList<>(jobhunterWorkRecordVO.size());
        for (AddTJobhunterWorkRecordVO tJobhunterWorkRecordVO : jobhunterWorkRecordVO) {
            TJobhunterWorkRecord tJobhunterWorkRecord=new TJobhunterWorkRecord();
            BeanUtils.copyProperties(tJobhunterWorkRecordVO,tJobhunterWorkRecord);
            tJobhunterWorkRecord.setResumeId(s);
            tJobhunterWorkRecord.setWorkId(idGenerator.generateCode("JW"));
            tJobhunterWorkRecord.setCreateTime(LocalDateTime.now());
            list.add(tJobhunterWorkRecord);
        }
        return this.saveBatch(list);
    }

    @Override
    public String getWorkId(String userid) {
        return tJobhunterWorkRecordMapper.getWorkId(userid);
    }

    @Override
    public Integer selectCount(String userId) {
        return tJobhunterWorkRecordMapper.selectCountId(userId);
    }
}




