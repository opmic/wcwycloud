package com.wcwy.company.service;

import com.wcwy.company.entity.TPermission;
import com.wcwy.company.entity.TRecommendedCompanies;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_recommended_companies(推荐官企业表)】的数据库操作Service
* @createDate 2022-12-08 17:20:30
*/
public interface TRecommendedCompaniesService extends IService<TRecommendedCompanies> {

    List<TPermission> rolePermission(String userId);
}
