package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.CompanyGlanceOverDTO;
import com.wcwy.company.entity.CompanyGlanceOver;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.vo.SelectCompanyGlanceOver;

/**
* @author Administrator
* @description 针对表【company_glance_over(企业浏览表)】的数据库操作Service
* @createDate 2022-11-01 09:29:51
*/
public interface CompanyGlanceOverService extends IService<CompanyGlanceOver> {

    IPage<CompanyGlanceOverDTO> seletcPage(SelectCompanyGlanceOver selectCompanyGlanceOver);

    void addCompanyGlanceOver(CompanyGlanceOver companyGlanceOver);
}
