package com.wcwy.company.mapper;

import com.wcwy.company.entity.TJobhunterExpectPosition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_jobhunter_expect_position(期望职位表)】的数据库操作Mapper
* @createDate 2022-10-08 11:58:41
* @Entity com.wcwy.company.entity.TJobhunterExpectPosition
*/
@Mapper
public interface TJobhunterExpectPositionMapper extends BaseMapper<TJobhunterExpectPosition> {
    @Select("SELECT t_jobhunter_expect_position.postion_id FROM t_jobhunter_resume INNER JOIN t_jobhunter_expect_position ON  t_jobhunter_resume.resume_id = t_jobhunter_expect_position.resume_id WHERE t_jobhunter_resume.t_jobhunter_id = #{userid} LIMIT 1")
    String postionId(String userid);
    @Select("SELECT COUNT(*) FROM t_jobhunter_expect_position INNER JOIN t_jobhunter_resume ON t_jobhunter_expect_position.resume_id = t_jobhunter_resume.resume_id WHERE t_jobhunter_resume.t_jobhunter_id = #{userId}")
    Integer selectCounts(String userId);

    List<String> selectPositionName(@Param("userid") String userid);
}




