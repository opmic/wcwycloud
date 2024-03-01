package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.dto.CompanyGlanceOverDTO;
import com.wcwy.company.entity.CompanyGlanceOver;
import com.wcwy.company.service.CompanyGlanceOverService;
import com.wcwy.company.mapper.CompanyGlanceOverMapper;
import com.wcwy.company.vo.SelectCompanyGlanceOver;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @description 针对表【company_glance_over(企业浏览表)】的数据库操作Service实现
 * @createDate 2022-11-01 09:29:51
 */
@Service
public class CompanyGlanceOverServiceImpl extends ServiceImpl<CompanyGlanceOverMapper, CompanyGlanceOver>
        implements CompanyGlanceOverService {
    @Resource
    private CompanyGlanceOverMapper companyGlanceOverMapper;

    @Override
    public IPage<CompanyGlanceOverDTO> seletcPage(SelectCompanyGlanceOver selectCompanyGlanceOver) {
        return companyGlanceOverMapper.seletcPage(selectCompanyGlanceOver.createPage(),selectCompanyGlanceOver);
    }

    @Override
    @Async
    public void addCompanyGlanceOver(CompanyGlanceOver companyGlanceOver) {
        this.save(companyGlanceOver);
    }
}




