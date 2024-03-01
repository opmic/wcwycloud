package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.dto.InterviewResumePostDTO;
import com.wcwy.company.entity.InterviewResume;
import com.wcwy.company.service.InterviewResumeService;
import com.wcwy.company.mapper.InterviewResumeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author Administrator
* @description 针对表【interview_resume(面试邀请表)】的数据库操作Service实现
* @createDate 2022-10-31 09:28:20
*/
@Service
public class InterviewResumeServiceImpl extends ServiceImpl<InterviewResumeMapper, InterviewResume>
    implements InterviewResumeService{

    @Resource
    private InterviewResumeMapper interviewResumeMapper;

    @Override
    public IPage<InterviewResumePostDTO> selectInterviewResumePost(Integer take, String userid, Page page) {
        return interviewResumeMapper.selectInterviewResumePost(take,userid,page);
    }
}




