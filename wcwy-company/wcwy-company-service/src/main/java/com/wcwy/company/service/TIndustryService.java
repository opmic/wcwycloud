package com.wcwy.company.service;

import com.wcwy.company.dto.TIndustryDTO;
import com.wcwy.company.entity.TIndustry;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_industry(行业信息表)】的数据库操作Service
* @createDate 2022-09-02 14:20:17
*/
public interface TIndustryService extends IService<TIndustry> {

    List<TIndustryDTO> selects();
}
