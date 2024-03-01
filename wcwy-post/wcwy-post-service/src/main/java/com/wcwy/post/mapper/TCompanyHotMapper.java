package com.wcwy.post.mapper;

import com.wcwy.company.vo.TCompanyUpdateVo;
import com.wcwy.post.entity.TCompanyHot;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【t_company_hot(企业热度表)】的数据库操作Mapper
* @createDate 2023-01-12 11:31:51
* @Entity com.wcwy.post.entity.TCompanyHot
*/
@Mapper
public interface TCompanyHotMapper extends BaseMapper<TCompanyHot> {

    boolean updateCompany(@Param("map") TCompanyUpdateVo map,@Param("industry") String industry);
}




