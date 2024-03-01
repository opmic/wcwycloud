package com.wcwy.company.service;

import com.wcwy.company.entity.TCompanyRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_company_role】的数据库操作Service
* @createDate 2022-09-07 13:49:20
*/

public interface TCompanyRoleService extends IService<TCompanyRole> {

    boolean add(String companyId);
}
