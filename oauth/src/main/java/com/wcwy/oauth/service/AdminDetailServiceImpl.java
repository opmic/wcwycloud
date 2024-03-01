package com.wcwy.oauth.service;

import com.alibaba.fastjson.JSON;
import com.wawy.company.api.TCompanyLoginApi;
import com.wcwy.company.entity.AdminUser;
import com.wcwy.company.entity.TCompany;
import com.wcwy.company.entity.TPermission;
import com.wcwy.oauth.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: AdUserDetailServiceImpl
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-24 21:37
 */
@Service
public class AdminDetailServiceImpl implements UserDetailsService {


    @Autowired
   private TCompanyLoginApi tCompanyLoginApi;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      /*  String substring = username.substring(0, 4);
        if(substring.equals("wcwy")){

        }*/
        //将来连接数据库根据账号查询用户信息
        AdminUser oneAdmin = tCompanyLoginApi.getOneAdmin(username);
        if(oneAdmin == null){
            //如果用户查不到，返回null，由provider来抛出异常
            throw  new  UsernameNotFoundException("该用户不存在");
        }
      /*  if(oneAdmin.getStatus().equals("1")){
            throw new InvalidClientException("用户已经被禁用");
        }*/
        //根据用户的id查询用户的权限
        List<TPermission> permissions = tCompanyLoginApi.roleAdminPermission(oneAdmin.getAdminId());
        List<String>permissionsCode=new ArrayList<>();
        for (TPermission permission : permissions) {
            permissionsCode.add( permission.getCode());
        }
        //将permissions转成数组
        String[] permissionArray = new String[permissions.size()];
        permissionsCode.toArray(permissionArray);
        //使用一个共有的user类存储用户信息
        LoginUser loginUser= new LoginUser();
        loginUser.setId(oneAdmin.getAdminId());
        loginUser.setUsername(oneAdmin.getAdminName());
        //loginUser
        loginUser.setUserMobile(oneAdmin.getLoginName());
        String principal = JSON.toJSONString(loginUser);

        UserDetails userDetails = User.withUsername(principal).password(oneAdmin.getPassword()).authorities(permissionArray).build();
        return userDetails;
    }

}