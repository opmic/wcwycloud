package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.dto.CompanyUserRoleDTO;
import com.wcwy.company.entity.CompanyUserRole;
import com.wcwy.company.query.CompanyUserRoleQuery;
import com.wcwy.company.service.CompanyUserRoleService;
import com.wcwy.company.mapper.CompanyUserRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Administrator
* @description 针对表【company_user_role(企业查看用户简历权限表)】的数据库操作Service实现
* @createDate 2023-04-03 20:12:47
*/
@Service
public class CompanyUserRoleServiceImpl extends ServiceImpl<CompanyUserRoleMapper, CompanyUserRole>
    implements CompanyUserRoleService{
    @Resource
   private CompanyUserRoleMapper companyUserRoleMapper;
    @Override
    public IPage<CompanyUserRoleDTO> select(CompanyUserRoleQuery companyUserRoleQuery,String userId) {
        return companyUserRoleMapper.select(companyUserRoleQuery.createPage(),userId,companyUserRoleQuery);
    }

    @Override
    public Boolean isDownload(String userId, String companyId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("company_id", companyId);
        queryWrapper.eq("deleted", 0);
        int count = this.count(queryWrapper);
       return count>0 ? true:false;
    }

    @Override
    public List<CompanyUserRoleDTO> selectWorkEducation(List<String> collect) {
        return companyUserRoleMapper.selectWorkEducation(collect);
    }
}




