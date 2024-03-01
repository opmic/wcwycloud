package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.TPermission;
import com.wcwy.company.entity.TRecommendedCompanies;
import com.wcwy.company.mapper.TRecommendedCompaniesRoleMapper;
import com.wcwy.company.service.TRecommendedCompaniesService;
import com.wcwy.company.mapper.TRecommendedCompaniesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_recommended_companies(推荐官企业表)】的数据库操作Service实现
* @createDate 2022-12-08 17:20:30
*/
@Service
public class TRecommendedCompaniesServiceImpl extends ServiceImpl<TRecommendedCompaniesMapper, TRecommendedCompanies>
    implements TRecommendedCompaniesService{
    @Autowired
    private TRecommendedCompaniesRoleMapper tRecommendedCompaniesRoleMapper;
    @Override
    public List<TPermission> rolePermission(String userId) {
        return tRecommendedCompaniesRoleMapper.rolePermission(userId);
    }
}




