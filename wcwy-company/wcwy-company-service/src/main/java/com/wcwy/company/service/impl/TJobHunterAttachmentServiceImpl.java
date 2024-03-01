package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.entity.TJobHunterAttachment;
import com.wcwy.company.service.TJobHunterAttachmentService;
import com.wcwy.company.mapper.TJobHunterAttachmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
* @author Administrator
* @description 针对表【t_job_hunter_attachment(求职者附件)】的数据库操作Service实现
* @createDate 2023-03-13 14:03:58
*/
@Service
public class TJobHunterAttachmentServiceImpl extends ServiceImpl<TJobHunterAttachmentMapper, TJobHunterAttachment>
    implements TJobHunterAttachmentService{

    @Autowired
    private IDGenerator idGenerator;
    @Override
    public Boolean adds(String resumePath, String userId) {
        TJobHunterAttachment tJobHunterAttachment=new TJobHunterAttachment();
        tJobHunterAttachment.setAttachmentId(idGenerator.generateCode("AC"));
        tJobHunterAttachment.setPath(resumePath);
        tJobHunterAttachment.setTJobHunterId(userId);
        tJobHunterAttachment.setCreateDate(LocalDate.now());
        tJobHunterAttachment.setTop(0);

        return this.save(tJobHunterAttachment);
    }
}




