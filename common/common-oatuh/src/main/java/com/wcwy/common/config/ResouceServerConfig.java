package com.wcwy.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration
@EnableResourceServer
@Slf4j
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {

    public static final String RESOURCE_ID = "res1";

    @Autowired
    TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        log.info("======================开启SpringSecurity===============");
        resources.resourceId(RESOURCE_ID)//资源 id
                .tokenStore(tokenStore)
//                .tokenServices(tokenService())//验证令牌的服务
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.info("======================开启SpringSecurity放行===============");
        http.authorizeRequests()
                .antMatchers("/wx-pay/cc").permitAll()//微信支付内部调用
                .antMatchers("/websocket/**","/sendMessage/sendMsg","/systemWebSocket/**").permitAll()//webSocket
                .antMatchers("/tUser/**","/test","/tPermission/**","/testRLock","/captcha/**","/jobhunter/insertCodeJobHunter","/login/**","/sms/**","/tCompanyType/**").permitAll()
                .antMatchers("/v2/api-docs","/tCompanyFirmSize/**","/tProvinces/**","/tCompanyType/**","/tPosition/**","/tIndustry/**").permitAll()
                .antMatchers("/company/insertCompany","/company/selectTCompany/**","/company/basicInformation/**","/company/verificationUrl/**","/company/phoneBinding","/company/companyLists").permitAll()//企业开放端
                .antMatchers("/company/selectPhone").permitAll()//企业获取电话号码
                .antMatchers("/education/**").permitAll()//学历
                .antMatchers("/file/**").permitAll()//学历
                .antMatchers("/major/**").permitAll()//专业名称
                .antMatchers("/tRecommend/insert","/recommendBasics/save","/tRecommend/selectBasic","/tRecommend/enterpriseInviter").permitAll()//推荐官
                .antMatchers("/tCompanyPost/selectPostId/**","/tCompanyPost/onLinePost","/tCompanyPost/hot","/tCompanyPost/latestPost/**","/tCompanyPost/selectOne/**","/tCompanyPost/jobType/**","/tCompanyPost/openForPositions","/tCompanyPost/jobSearch","/tCompanyPost/firmPost","/tCompanyPost/workPlace","/tCompanyPost/basic").permitAll()//获取在线岗位数量
                .antMatchers("/tPostShare/select").permitAll()
                .antMatchers("/topUp/updateCurrencyCount").permitAll()//微信支付内部调用
                .antMatchers("/school/**").permitAll()//school
                .antMatchers("/cooTribe/page","/cooTribe/topSearch").permitAll()//coo
                .antMatchers("/tPosition/**").permitAll()//职位
                .antMatchers("/postWeal/**").permitAll()//职位福利
                .antMatchers("/work/**").permitAll()//工作经验接口开发
                .antMatchers("/wxLogin/state").permitAll()//微信开发工具类
                .antMatchers("/wxLogin/weixinLogin").permitAll()//微信开发工具类
                .antMatchers("/tCompanyPost/selectNewest","/tCompanyPost/home","/tCompanyPost/hotJob","/tCompanyPost/tCompanyHotPostDTOS","/tCompanyPost/schoolyard","/tCompanyPost/countSchoolyard").permitAll()//岗位接口
                .antMatchers("/company/tCompanyById").permitAll()//企业信息
                .antMatchers("/wx-pay/native/notify").permitAll()//微信支付回调
                .antMatchers("/putInResume/updateDownloadResume","/putInResume/companyDate").permitAll()//更改企业下载状态
                .antMatchers("/tRecommendedCompanies/addTRecommendedAcademy").permitAll()//校园注册
                .antMatchers("/tRecommendedCompanies/addTRecommendedCompanies").permitAll()//企业推荐官注册
                .antMatchers("/tRecommendedCompanies/forgetThePassword").permitAll()//企业推荐官注册
                .antMatchers("/tRecommendedCompanies/verificationNumber").permitAll()//企业推荐官注册
                .antMatchers("/tPostShare/**").permitAll()//企业推荐官注册
                .antMatchers("/company/insertCompanySubsidiaryVo").permitAll()//子企业注册
                .antMatchers("/collerct/isCollect").permitAll()//收藏
                .antMatchers("/jobhunter/insertJobHunter").permitAll()//求职者注册
                .antMatchers("/token/accessToken").permitAll()//开发访问令牌/tCompanyPost/list
                .antMatchers("/information/**").permitAll()//咨询开放权限
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/QRCode/**").permitAll()//二维码的接口放行
                .antMatchers("/shareData/**").permitAll()//二维码的接口放行
                .antMatchers("/mistake/**").permitAll()//二维码的接口放行
                .antMatchers("/accessRecord/**").permitAll()//访问记录放行
                .antMatchers("/tRefundInfo/selectTPayConfig").permitAll()//支付兑换配置接口
              /*  .antMatchers("/tCompanyPost/list").permitAll()//开放补充岗位基础信息接口*/
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/**/*.html"
                ).permitAll()
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/doc.html").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/webjars/**").anonymous()
                .antMatchers("/**").access("#oauth2.hasScope('ROLE_ADMIN')")
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
