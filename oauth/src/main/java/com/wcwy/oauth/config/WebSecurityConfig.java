package com.wcwy.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ClassName: WebSecurityConfig
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-24 17:23
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //认证管理器
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    //密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //安全拦截机制（最重要）
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login*","/oauth/**","/authorization/**").permitAll()
                .anyRequest().permitAll()//除了/r/**，其它的请求可以访问
                .and()
                .formLogin()
                .and().httpBasic()
        ;
    }

    public static void main(String[] args) {
     BCryptPasswordEncoder bCryptPasswordEncoder= new   BCryptPasswordEncoder();
        String wcwy = bCryptPasswordEncoder.encode("tangzhuonb");
        System.out.println(wcwy);
    }
}
