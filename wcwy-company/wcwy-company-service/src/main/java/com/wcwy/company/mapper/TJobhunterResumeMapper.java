package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.company.dto.DetailedTJobhunterResumeDTO;
import com.wcwy.company.dto.ReferrerRecordJobHunterDTO;
import com.wcwy.company.entity.TJobhunterResume;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.query.CompanyInviteJobHunterQuery;
import com.wcwy.company.query.SendAResumeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author Administrator
* @description 针对表【t_jobhunter_resume(求职者简历)】的数据库操作Mapper
* @createDate 2022-10-21 10:09:56
* @Entity com.wcwy.company.entity.TJobhunterResume
*/
@Mapper
public interface TJobhunterResumeMapper extends BaseMapper<TJobhunterResume> {

    DetailedTJobhunterResumeDTO detailedTJobhunterResumeDTO(@Param("jobhunterId") String jobhunterId);

    Page<DetailedTJobhunterResumeDTO> sendAResume(@Param("page") IPage page , @Param("sendAResumeQuery") SendAResumeQuery sendAResumeQuery);

    int inviter(@Param("userid") String userid,@Param("resumeId") String resumeId);
    @Select("SELECT t_jobhunter_resume.advantage FROM t_jobhunter_resume WHERE t_jobhunter_resume.t_jobhunter_id = #{userid} ")
    String getAdvantage(String userid);

    /**
     * 人才私域
     * @param companyInviteJobHunterQuery
     * @return
     */
    IPage<ReferrerRecordJobHunterDTO> companyInviteJobHunter(@Param("page") IPage page,@Param("companyInviteJobHunterQuery")  CompanyInviteJobHunterQuery companyInviteJobHunterQuery);
}




