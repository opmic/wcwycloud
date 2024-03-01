package com.wcwy.company.mapper;

import com.wcwy.company.dto.TProvincesCitieDTO;
import com.wcwy.company.entity.TProvinces;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_provinces(省份)】的数据库操作Mapper
* @createDate 2022-09-02 10:33:39
* @Entity com.wcwy.company.entity.TProvinces
*/
@Mapper
public interface TProvincesMapper extends BaseMapper<TProvinces> {

    List<TProvincesCitieDTO> selectPC();
    @Select("SELECT t_recommend.phone FROM t_recommend WHERE t_recommend.id =#{putInUser} ")
    String selectPhone(String putInUser);
}




