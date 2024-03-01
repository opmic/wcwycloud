package com.wcwy.oauth.mobile;

import com.alibaba.fastjson.JSON;
import com.wawy.company.api.TCompanyLoginApi;
import com.wcwy.common.base.enums.SmsEunm;
import com.wcwy.oauth.dao.RMUserDao;
import com.wcwy.oauth.dao.TJUserDao;
import com.wcwy.oauth.entity.LoginUser;
import com.wcwy.oauth.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ClassName: DemoUserDetailsServiceImpl
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-24 20:46
 */
@Service
public class RMUserDetailsServiceImpl implements RMUserDetailsService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    RMUserDao rmUserDao;
    @Autowired
    private TCompanyLoginApi tCompanyLoginApi;
/*    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //将来连接数据库根据账号查询用户信息
        UserDto userDto = userDao.getUserByUsername(username);
        if(userDto == null){
            //如果用户查不到，返回null，由provider来抛出异常
            throw  new  UsernameNotFoundException("该用户不存在");
        }
        //根据用户的id查询用户的权限
        List<String> permissions = userDao.findPermissionsByUserId(userDto.getId());
        //将permissions转成数组
        String[] permissionArray = new String[permissions.size()];
        permissions.toArray(permissionArray);
        //将userDto转成json
        String principal = JSON.toJSONString(userDto);
        UserDetails userDetails = User.withUsername(principal).password(userDto.getPassword()).authorities(permissionArray).build();
        return userDetails;
    }*/

    @Override
    public UserDetails loadUserByMobile(String s) throws UsernameNotFoundException {
        //将来连接数据库根据账号查询用户信息
        UserDto userDto = rmUserDao.getUserByUsername(s);
        if(userDto == null){
            //如果用户查不到，返回null，由provider来抛出异常
            return null;
        }
        //根据用户的id查询用户的权限
        List<String> permissions = rmUserDao.findPermissionsByUserId(userDto.getId());
        //将permissions转成数组
        String[] permissionArray = new String[permissions.size()];
        permissions.toArray(permissionArray);
        //将userDto转成json
        //String principal = JSON.toJSONString(permissionArray);
        LoginUser loginUser= new LoginUser();
        loginUser.setId(userDto.getId());
        loginUser.setUsername(userDto.getUsername());
        //loginUser
        loginUser.setUserMobile(userDto.getMobile());
        String principal = JSON.toJSONString(loginUser);
        UserDetails userDetails = User.withUsername(principal).password(userDto.getPassword()).authorities(permissionArray).build();
        return userDetails;
    }

    @Override
    public Boolean verificationCode(String code,String phone) {
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringValueOperations.get(SmsEunm.LOGIN_CODE.getType() + phone);
        if(StringUtils.isEmpty(s)){
            throw  new UsernameNotFoundException("验证码不存在或已过期,请重新获取验证码");
        }

        if(! s.equals(s)){
            throw  new UsernameNotFoundException("验证码不正确!");
        }
        stringRedisTemplate.delete(SmsEunm.LOGIN_CODE.getType() + phone);
        return true;
    }

}
