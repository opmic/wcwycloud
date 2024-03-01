package com.wcwy.company.mapper;

import com.wcwy.company.entity.TCompanyContract;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author Administrator
* @description 针对表【t_company_contract(企业合同)】的数据库操作Mapper
* @createDate 2022-11-17 17:20:54
* @Entity com.wcwy.company.entity.TCompanyContract
*/
@Mapper
public interface TCompanyContractMapper extends BaseMapper<TCompanyContract> {


    @Select("SELECT * FROM t_company_contract where t_company_contract.create_id =#{userid} ORDER BY t_company_contract.create_time DESC LIMIT 1")
    TCompanyContract selectOne(@Param("userid") String userid);
    @Select("SELECT	* FROM	t_company_contract WHERE	t_company_contract.create_id = #{userid} AND	t_company_contract.state = 0 ORDER BY	t_company_contract.create_time ASC")
    TCompanyContract one(@Param("userid") String userid);
}




