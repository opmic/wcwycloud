package com.wcwy.company.mapper;

import com.wcwy.company.entity.TCities;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【t_cities(城市信息表)】的数据库操作Mapper
* @createDate 2022-09-02 10:33:31
* @Entity com.wcwy.company.entity.TCities
*/
@Mapper
public interface TCitiesMapper extends BaseMapper<TCities> {

}




