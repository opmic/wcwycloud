package com.wcwy.company.service;

import com.wcwy.company.entity.TJobHunterAttachment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_job_hunter_attachment(求职者附件)】的数据库操作Service
* @createDate 2023-03-13 14:03:58
*/
public interface TJobHunterAttachmentService extends IService<TJobHunterAttachment> {

    /**
     * 添加附件
     * @param resumePath
     * @param userId
     * @return
     */
    Boolean adds(String resumePath, String userId);
}
