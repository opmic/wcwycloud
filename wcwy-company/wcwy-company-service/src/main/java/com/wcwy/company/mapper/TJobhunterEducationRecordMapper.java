package com.wcwy.company.mapper;

import com.wcwy.company.dto.JobHunterEducationRecordDTO;
import com.wcwy.company.entity.TJobhunterEducationRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_jobhunter_education_record(教育经历表)】的数据库操作Mapper
* @createDate 2022-10-08 11:58:38
* @Entity com.wcwy.company.entity.TJobhunterEducationRecord
*/
@Mapper
public interface TJobhunterEducationRecordMapper extends BaseMapper<TJobhunterEducationRecord> {
    @Select("SELECT t_jobhunter_education_record.edu_id FROM t_jobhunter_resume INNER JOIN t_jobhunter_education_record ON  t_jobhunter_resume.resume_id = t_jobhunter_education_record.resume_id WHERE t_jobhunter_resume.t_jobhunter_id =#{userid}  LIMIT 1 ")
    String getEduId(String userid);
    @Select("SELECT COUNT(t_jobhunter_education_record.edu_id) FROM t_jobhunter_resume INNER JOIN t_jobhunter_education_record ON t_jobhunter_resume.resume_id = t_jobhunter_education_record.resume_id WHERE t_jobhunter_resume.t_jobhunter_id = #{userId}")
    Integer count(String userId);
    @Select("SELECT t_jobhunter_resume.t_jobhunter_id as jobHunterId,t_jobhunter_education_record.education FROM t_jobhunter_education_record INNER JOIN t_jobhunter_resume ON t_jobhunter_education_record.resume_id = t_jobhunter_resume.resume_id")
    List<JobHunterEducationRecordDTO> correct();
}




