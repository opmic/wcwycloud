package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.CompanyUserRoleDTO;
import com.wcwy.company.entity.CompanyUserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.query.CompanyUserRoleQuery;

import java.util.List;

/**
* @author Administrator
* @description 针对表【company_user_role(企业查看用户简历权限表)】的数据库操作Service
* @createDate 2023-04-03 20:12:47
*/
public interface CompanyUserRoleService extends IService<CompanyUserRole> {

    /**
     * 企业查询下载的求职者
     * @param companyUserRoleQuery
     * @return
     */
    IPage<CompanyUserRoleDTO> select(CompanyUserRoleQuery companyUserRoleQuery,String userId);

    /**
     * @Description:是否下载
     * @param userId：求职者id  companyId:企业id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/8/7 17:04
     */

    Boolean isDownload(String userId, String companyId);

    /**
     * 查询最高学历及最近工作经验
     * @param collect
     * @return
     */
    List<CompanyUserRoleDTO> selectWorkEducation(List<String> collect);
}
