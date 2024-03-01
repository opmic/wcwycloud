package com.wcwy.company.mapper;

import com.wcwy.company.entity.School;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【school(学校表)】的数据库操作Mapper
* @createDate 2022-09-08 16:01:15
* @Entity com.wcwy.company.entity.School
*/
@Mapper
public interface SchoolMapper extends BaseMapper<School> {

}




