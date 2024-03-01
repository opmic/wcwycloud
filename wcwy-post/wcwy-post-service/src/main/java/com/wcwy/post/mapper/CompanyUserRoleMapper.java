package com.wcwy.post.mapper;

import com.wcwy.post.entity.CompanyUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【company_user_role(企业查看用户简历权限表)】的数据库操作Mapper
* @createDate 2022-10-17 13:47:26
* @Entity com.wcwy.post.entity.CompanyUserRole
*/
@Mapper
public interface CompanyUserRoleMapper extends BaseMapper<CompanyUserRole> {

}




