package com.wcwy.company.mapper;

import com.wcwy.company.dto.TIndustryDTO;
import com.wcwy.company.entity.TIndustry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_industry(行业信息表)】的数据库操作Mapper
* @createDate 2022-09-02 14:20:17
* @Entity com.wcwy.company.entity.TIndustry
*/
@Mapper
public interface TIndustryMapper extends BaseMapper<TIndustry> {

    List<TIndustryDTO> selects();
}




