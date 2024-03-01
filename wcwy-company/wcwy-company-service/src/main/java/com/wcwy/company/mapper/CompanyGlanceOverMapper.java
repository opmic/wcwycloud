package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.CompanyGlanceOverDTO;
import com.wcwy.company.entity.CompanyGlanceOver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.vo.SelectCompanyGlanceOver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【company_glance_over(企业浏览表)】的数据库操作Mapper
* @createDate 2022-11-01 09:29:51
* @Entity com.wcwy.company.entity.CompanyGlanceOver
*/
@Mapper
public interface CompanyGlanceOverMapper extends BaseMapper<CompanyGlanceOver> {

    IPage<CompanyGlanceOverDTO> seletcPage(@Param("page") IPage page, @Param("selectCompanyGlanceOver") SelectCompanyGlanceOver selectCompanyGlanceOver);
}




