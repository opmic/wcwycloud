package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.TCompanyRole;
import com.wcwy.company.mapper.TCompanyMapper;
import com.wcwy.company.service.TCompanyRoleService;
import com.wcwy.company.mapper.TCompanyRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author Administrator
* @description 针对表【t_company_role】的数据库操作Service实现
* @createDate 2022-09-07 13:49:20
*/
@Service
public class TCompanyRoleServiceImpl extends ServiceImpl<TCompanyRoleMapper, TCompanyRole>
    implements TCompanyRoleService{

    @Autowired
    private TCompanyRoleMapper tCompanyRoleMapper;
    @Override
    public boolean add(String companyId) {
        TCompanyRole tCompanyRole=new TCompanyRole();
        tCompanyRole.setUserId(companyId);
        tCompanyRole.setCreateTime(LocalDateTime.now());
        tCompanyRole.setRoleId("1");
        int insert = tCompanyRoleMapper.insert(tCompanyRole);
        return insert>0? true:false;
    }
}




