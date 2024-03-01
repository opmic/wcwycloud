package com.wcwy.post.service;

import com.wcwy.company.vo.TCompanyUpdateVo;
import com.wcwy.post.entity.TCompanyHot;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_company_hot(企业热度表)】的数据库操作Service
* @createDate 2023-01-12 11:31:51
*/
public interface TCompanyHotService extends IService<TCompanyHot> {

    /**
     * 更新企业基本信息
     * @param map
     * @return
     */
    boolean updateCompany(TCompanyUpdateVo map);
}
