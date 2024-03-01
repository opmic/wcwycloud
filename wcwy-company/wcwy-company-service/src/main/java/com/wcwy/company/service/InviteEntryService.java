package com.wcwy.company.service;

import com.wcwy.company.dto.InviteEntryDTO;
import com.wcwy.company.dto.InviteEntryPutInResume;
import com.wcwy.company.dto.NotEntryDTO;
import com.wcwy.company.dto.OneInterviewDTO;
import com.wcwy.company.entity.InviteEntry;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【invite_entry(发送offer表)】的数据库操作Service
* @createDate 2022-11-08 16:23:16
*/
public interface InviteEntryService extends IService<InviteEntry> {

    /**
     * @Description: 获取入职付和按月付投简
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/1/3 16:38
     */

    List<InviteEntryPutInResume> selectList();

    InviteEntryPutInResume selectListPutInResumeId(String putInResumeId);

    InviteEntryDTO selectResumeId(String resumeId);

    String selectCancelCause(String inviteEntryId);

    /**
     * 查询未入职申请原因
     * @param putInResumeId
     * @return
     */
    NotEntryDTO notEntry(String putInResumeId);

    /**
     * 获取第一场非取消的面试
     * @param userid
     * @param putInResumeId
     * @return
     */
    OneInterviewDTO oneInterview(String userid, String putInResumeId);
}
