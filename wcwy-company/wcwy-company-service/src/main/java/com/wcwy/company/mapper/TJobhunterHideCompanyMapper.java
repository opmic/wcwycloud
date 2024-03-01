package com.wcwy.company.mapper;

import com.wcwy.company.entity.TJobhunterHideCompany;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_jobhunter_hide_company(用户屏蔽公司表)】的数据库操作Mapper
* @createDate 2022-10-08 11:58:44
* @Entity com.wcwy.company.entity.TJobhunterHideCompany
*/
@Mapper
public interface TJobhunterHideCompanyMapper extends BaseMapper<TJobhunterHideCompany> {

    /**
     * @Description: 查询屏蔽的企业
     * @param
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/8/7 13:43
     */

    @Select("SELECT DISTINCT t_company.company_id FROM t_jobhunter_hide_company INNER JOIN t_company ON t_jobhunter_hide_company.company_name = t_company.company_name WHERE t_jobhunter_hide_company.resume_id = #{jobHunter}")
    List<Object> cacheCompany(@Param("jobHunter") String jobHunter);
}




