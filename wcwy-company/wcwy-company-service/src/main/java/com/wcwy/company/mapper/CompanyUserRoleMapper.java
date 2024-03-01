package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.CompanyUserRoleDTO;
import com.wcwy.company.entity.CompanyUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.query.CompanyUserRoleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【company_user_role(企业查看用户简历权限表)】的数据库操作Mapper
* @createDate 2023-04-03 20:12:47
* @Entity com.wcwy.company.entity.CompanyUserRole
*/
@Mapper
public interface CompanyUserRoleMapper extends BaseMapper<CompanyUserRole> {

    /**
     * 企业查询下载的求职者
     * @param page
     * @param companyUserRoleQuery
     * @return
     */
    IPage<CompanyUserRoleDTO> select(@Param("page") IPage page,@Param("userId") String userId, @Param("companyUserRoleQuery") CompanyUserRoleQuery companyUserRoleQuery);

    List<CompanyUserRoleDTO> selectWorkEducation(@Param("collect")  List<String> collect);
}




