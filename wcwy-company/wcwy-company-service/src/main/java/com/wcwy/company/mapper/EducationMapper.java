package com.wcwy.company.mapper;

import com.wcwy.company.entity.Education;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【education(学历表)】的数据库操作Mapper
* @createDate 2022-09-07 14:08:36
* @Entity com.wcwy.company.entity.Education
*/
@Mapper
public interface EducationMapper extends BaseMapper<Education> {

}




