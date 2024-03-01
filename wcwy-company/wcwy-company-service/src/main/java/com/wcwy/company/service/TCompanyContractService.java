package com.wcwy.company.service;

import com.wcwy.company.entity.TCompanyContract;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_company_contract(企业合同)】的数据库操作Service
* @createDate 2022-11-17 17:20:54
*/
public interface TCompanyContractService extends IService<TCompanyContract> {

    /**
     * 查看最新合同
     * @param userid
     * @return
     */
    TCompanyContract selectOne(String userid);

    /***
     * 获取最新的合同记录
     * @param userid
     * @return
     */
    TCompanyContract one(String userid);
}
