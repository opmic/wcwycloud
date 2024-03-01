package com.wcwy.oauth.mobile;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @ClassName: DemoUserDetailsService
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-24 20:46
 */
public interface RMUserDetailsService {

    UserDetails loadUserByMobile(String s) throws UsernameNotFoundException;

    /**
     * 验证验证码
     * @return
     */
    Boolean verificationCode(String code,String phone);

}
