package com.wcwy.post.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.vo.TCompanyUpdateVo;
import com.wcwy.post.entity.TCompanyHot;
import com.wcwy.post.service.TCompanyHotService;
import com.wcwy.post.mapper.TCompanyHotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_company_hot(企业热度表)】的数据库操作Service实现
* @createDate 2023-01-12 11:31:51
*/
@Service
public class TCompanyHotServiceImpl extends ServiceImpl<TCompanyHotMapper, TCompanyHot>
    implements TCompanyHotService{
    @Autowired
  private   TCompanyHotMapper tCompanyHotMapper;
    @Override
    public boolean updateCompany(TCompanyUpdateVo map) {
        return tCompanyHotMapper.updateCompany(map, JSON.toJSONString(map.getIndustry()));
    }
}




