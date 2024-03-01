package com.wcwy.company.mapper;

import com.wcwy.company.entity.TJobhunterWorkRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author Administrator
* @description 针对表【t_jobhunter_work_record(工作经历表)】的数据库操作Mapper
* @createDate 2022-10-08 11:58:59
* @Entity com.wcwy.company.entity.TJobhunterWorkRecord
*/
@Mapper
public interface TJobhunterWorkRecordMapper extends BaseMapper<TJobhunterWorkRecord> {
    @Select("SELECT  t_jobhunter_work_record.work_id FROM t_jobhunter_work_record INNER JOIN t_jobhunter_resume ON t_jobhunter_work_record.resume_id = t_jobhunter_resume.resume_id WHERE t_jobhunter_resume.t_jobhunter_id =#{userid} LIMIT 1" )
    String getWorkId(@Param("userid")String userid);
 @Select(" SELECT COUNT(t_jobhunter_work_record.work_id) FROM t_jobhunter_work_record INNER JOIN t_jobhunter_resume ON t_jobhunter_work_record.resume_id = t_jobhunter_resume.resume_id WHERE t_jobhunter_resume.t_jobhunter_id =#{userId}" )
    Integer selectCountId(@Param("userId") String userId);
}




