package com.wcwy.company.mapper;

import com.wcwy.company.entity.TJobhunterProjectRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author Administrator
* @description 针对表【t_jobhunter_project_record(项目经历表)】的数据库操作Mapper
* @createDate 2022-10-08 11:58:50
* @Entity com.wcwy.company.entity.TJobhunterProjectRecord
*/
@Mapper
public interface TJobhunterProjectRecordMapper extends BaseMapper<TJobhunterProjectRecord> {

    @Select("SELECT COUNT(t_jobhunter_project_record.project_id) FROM t_jobhunter_project_record INNER JOIN t_jobhunter_resume ON t_jobhunter_project_record.resume_id = t_jobhunter_resume.resume_id WHERE t_jobhunter_resume.t_jobhunter_id =#{userId} ")
    Integer selectCountId(@Param("userId")String userId);
}




