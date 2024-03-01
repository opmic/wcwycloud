package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.AdminUser;
import com.wcwy.company.entity.TPermission;
import com.wcwy.company.service.AdminUserService;
import com.wcwy.company.mapper.AdminUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【admin_user(管理员账号
)】的数据库操作Service实现
* @createDate 2022-10-11 15:49:47
*/
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser>
    implements AdminUserService{
    @Autowired
    private AdminUserMapper adminUserMapper;
    @Override
    public List<TPermission> rolePermission(String userId) {
        return adminUserMapper.rolePermission(userId);
    }
}




