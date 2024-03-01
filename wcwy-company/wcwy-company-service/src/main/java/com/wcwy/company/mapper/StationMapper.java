package com.wcwy.company.mapper;

import com.wcwy.company.entity.Station;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【station(岗位表)】的数据库操作Mapper
* @createDate 2022-09-02 14:49:27
* @Entity com.wcwy.company.entity.Station
*/
@Mapper
public interface StationMapper extends BaseMapper<Station> {

}




