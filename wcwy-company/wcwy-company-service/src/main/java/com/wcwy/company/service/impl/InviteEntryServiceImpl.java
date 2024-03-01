package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.dto.InviteEntryDTO;
import com.wcwy.company.dto.InviteEntryPutInResume;
import com.wcwy.company.dto.NotEntryDTO;
import com.wcwy.company.dto.OneInterviewDTO;
import com.wcwy.company.entity.InviteEntry;
import com.wcwy.company.service.InviteEntryService;
import com.wcwy.company.mapper.InviteEntryMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【invite_entry(发送offer表)】的数据库操作Service实现
 * @createDate 2022-11-08 16:23:16
 */
@Service
public class InviteEntryServiceImpl extends ServiceImpl<InviteEntryMapper, InviteEntry>
        implements InviteEntryService {

    @Resource
    private InviteEntryMapper inviteEntryMapper;

    @Override
    public List<InviteEntryPutInResume> selectList() {
        return inviteEntryMapper.select();
    }

    @Override
    public InviteEntryPutInResume selectListPutInResumeId(String putInResumeId) {
        return inviteEntryMapper.selectListPutInResumeId(putInResumeId);
    }

    @Override
    public InviteEntryDTO selectResumeId(String resumeId) {
        return inviteEntryMapper.selectResumeId(resumeId);
    }

    @Override
    public String selectCancelCause(String inviteEntryId) {
        return inviteEntryMapper.selectCancelCause(inviteEntryId);
    }

    @Override
    public NotEntryDTO notEntry(String putInResumeId) {
        return inviteEntryMapper.notEntry(putInResumeId);
    }

    @Override
    public OneInterviewDTO oneInterview(String userid, String putInResumeId) {
        return inviteEntryMapper.oneInterview(userid,putInResumeId);
    }
}




