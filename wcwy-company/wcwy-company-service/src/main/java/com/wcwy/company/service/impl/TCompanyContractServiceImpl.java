package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.TCompanyContract;
import com.wcwy.company.service.TCompanyContractService;
import com.wcwy.company.mapper.TCompanyContractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_company_contract(企业合同)】的数据库操作Service实现
* @createDate 2022-11-17 17:20:54
*/
@Service
public class TCompanyContractServiceImpl extends ServiceImpl<TCompanyContractMapper, TCompanyContract>
    implements TCompanyContractService{

    @Autowired
    private TCompanyContractMapper tCompanyContractMapper;

    @Override
    public TCompanyContract selectOne(String userid) {
        return tCompanyContractMapper.selectOne(userid);
    }

    @Override
    public TCompanyContract one(String userid) {

        return tCompanyContractMapper.one(userid);
    }
}




