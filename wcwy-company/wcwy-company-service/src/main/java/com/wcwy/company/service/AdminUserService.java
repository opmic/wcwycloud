package com.wcwy.company.service;

import com.wcwy.company.entity.AdminUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.entity.TPermission;

import java.util.List;

/**
* @author Administrator
* @description 针对表【admin_user(管理员账号
)】的数据库操作Service
* @createDate 2022-10-11 15:49:47
*/
public interface AdminUserService extends IService<AdminUser> {

    List<TPermission> rolePermission(String userId);
}
