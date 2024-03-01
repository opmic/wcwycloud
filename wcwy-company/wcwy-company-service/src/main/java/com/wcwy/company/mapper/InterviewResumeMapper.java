package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.company.dto.InterviewResumePostDTO;
import com.wcwy.company.entity.InterviewResume;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【interview_resume(面试邀请表)】的数据库操作Mapper
* @createDate 2022-10-31 09:28:20
* @Entity com.wcwy.company.entity.InterviewResume
*/
@Mapper
public interface InterviewResumeMapper extends BaseMapper<InterviewResume> {

    public int selel();

    IPage<InterviewResumePostDTO> selectInterviewResumePost(@Param("take") Integer take,@Param("userid") String userid,@Param("page") Page page);
}




