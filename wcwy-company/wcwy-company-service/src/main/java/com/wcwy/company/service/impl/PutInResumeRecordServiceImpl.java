package com.wcwy.company.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.entity.PutInResumeRecord;
import com.wcwy.company.service.PutInResumeRecordService;
import com.wcwy.company.mapper.PutInResumeRecordMapper;
import com.wcwy.company.vo.PutInResumeRecordVO;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Administrator
 * @description 针对表【put_in_resume_record(投简记录)】的数据库操作Service实现
 * @createDate 2023-05-15 10:03:41
 */
@Service
public class PutInResumeRecordServiceImpl extends ServiceImpl<PutInResumeRecordMapper, PutInResumeRecord>
        implements PutInResumeRecordService {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private PutInResumeRecordMapper putInResumeRecordMapper;
    @Override
    public void addRecord(String putInId, PutInResumeRecordVO putInResumeRecordVO) {
        long l = redisUtils.lGetListSize(Cache.CACHE_PUT_IN_RESUME_RECORD.getKey() + putInId);
        if(l==0){
            PutInResumeRecord byId = this.getById(putInId);
            if(byId !=null){
                List<Object> content = byId.getContent();
                redisUtils.lSets(Cache.CACHE_PUT_IN_RESUME_RECORD.getKey()+putInId,content);
            }
        }
        redisUtils.lSet(Cache.CACHE_PUT_IN_RESUME_RECORD.getKey()+putInId,putInResumeRecordVO);
        PutInResumeRecord putInResumeRecord=new PutInResumeRecord();
        putInResumeRecord.setPutInResumeId(putInId);
        List<Object> objects = redisUtils.lGet(Cache.CACHE_PUT_IN_RESUME_RECORD.getKey() + putInId, 0, redisUtils.lGetListSize(Cache.CACHE_PUT_IN_RESUME_RECORD.getKey() + putInId));
        putInResumeRecord.setContent(objects);
        this.saveOrUpdate(putInResumeRecord);
    }

    @Override
    public Map<String, String> phoneAndPost(String putPost) {
        return putInResumeRecordMapper.phoneAndPost(putPost);
    }

    @Override
    public Map<String, String> RmPhoneAndPost(String putPost) {
        return putInResumeRecordMapper.RmPhoneAndPost(putPost);
    }

    @Override
    public Map<String, String> RmPhoneAndCompany(String putPost) {
        return putInResumeRecordMapper.RmPhoneAndCompany(putPost);
    }
}




