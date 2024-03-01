package com.wcwy.company.mapper;

import com.wcwy.company.entity.TPermission;
import com.wcwy.company.entity.TRecommendedCompaniesRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_recommended_companies_role】的数据库操作Mapper
* @createDate 2022-12-08 17:20:21
* @Entity com.wcwy.company.entity.TRecommendedCompaniesRole
*/
@Mapper
public interface TRecommendedCompaniesRoleMapper extends BaseMapper<TRecommendedCompaniesRole> {

    List<TPermission> rolePermission(@Param("userId") String userId);
}




