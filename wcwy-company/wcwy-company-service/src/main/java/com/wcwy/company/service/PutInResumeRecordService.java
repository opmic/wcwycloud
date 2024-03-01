package com.wcwy.company.service;

import com.wcwy.company.entity.PutInResumeRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.vo.PutInResumeRecordVO;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * @author Administrator
 * @description 针对表【put_in_resume_record(投简记录)】的数据库操作Service
 * @createDate 2023-05-15 10:03:41
 */
public interface PutInResumeRecordService extends IService<PutInResumeRecord> {

    @Async
    void addRecord(String putInId, PutInResumeRecordVO putInResumeRecordVO);

    /**
     * 获取企业联系人电话号码
     * @param putPost
     * @return
     */
    Map<String, String> phoneAndPost(String putPost);

    /**
     * 获取投简人推荐官电话号码
     * @param putPost
     * @return
     */
    Map<String, String> RmPhoneAndPost(String putPost);

    /**
     * 获取企业名称及推荐官号码
     * @param putPost
     * @return
     */
    Map<String, String> RmPhoneAndCompany(String putPost);
}
