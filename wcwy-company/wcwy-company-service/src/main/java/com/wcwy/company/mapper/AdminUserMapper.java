package com.wcwy.company.mapper;

import com.wcwy.company.entity.AdminUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.entity.TPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【admin_user(管理员账号
)】的数据库操作Mapper
* @createDate 2022-10-11 15:49:47
* @Entity com.wcwy.company.entity.AdminUser
*/
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {

    List<TPermission> rolePermission(@Param("userId") String userId);
}




