package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.company.dto.InterviewResumePostDTO;
import com.wcwy.company.entity.InterviewResume;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【interview_resume(面试邀请表)】的数据库操作Service
* @createDate 2022-10-31 09:28:20
*/
public interface InterviewResumeService extends IService<InterviewResume> {


    /**
     * @Description: 获取我的申请
     * @param take:面试状态  userid:投简人id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/5/19 13:54
     */

    IPage<InterviewResumePostDTO> selectInterviewResumePost(Integer take, String userid, Page page);
}
