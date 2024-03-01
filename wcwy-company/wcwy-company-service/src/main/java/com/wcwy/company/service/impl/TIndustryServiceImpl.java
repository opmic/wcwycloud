package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.dto.TIndustryDTO;
import com.wcwy.company.entity.TIndustry;
import com.wcwy.company.service.TIndustryService;
import com.wcwy.company.mapper.TIndustryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_industry(行业信息表)】的数据库操作Service实现
* @createDate 2022-09-02 14:20:17
*/
@Service
public class TIndustryServiceImpl extends ServiceImpl<TIndustryMapper, TIndustry>
    implements TIndustryService{
    @Autowired
   private TIndustryMapper tIndustryMapper;
    @Override
    public List<TIndustryDTO> selects() {
        return tIndustryMapper.selects();
    }
}




