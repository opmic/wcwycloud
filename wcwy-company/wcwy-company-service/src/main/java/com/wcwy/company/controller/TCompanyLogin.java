package com.wcwy.company.controller;

/**
 * ClassName: TCompanyLogin
 * Description:做oauth内部调用
 * date: 2022/9/1 10:01
 *
 * @author tangzhuo
 * @since JDK 1.8
 */


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.base.constant.Constanst;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.AesUtil;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.company.asyn.LoginRecord;
import com.wcwy.company.entity.*;
import com.wcwy.company.service.*;

import com.wcwy.company.vo.AccessToken;
import com.wcwy.company.vo.WechatUserUnionID;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller
//@Api(tags ="做oauth内部调用" )
@RestController
@RequestMapping("/login")
public class TCompanyLogin {
    @Autowired
    private TCompanyService tCompanyService;
    @Autowired
    private TRecommendService tRecommendService;
    @Autowired
    private WeixinLoginService weixinLoginService;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private LoginRecord loginRecord;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private TRecommendedCompaniesService tRecommendedCompaniesService;
    /**
     * @param username 登录名
     * @return null
     * @Description: 获取企业信息
     * @Author tangzhuo
     * @CreateTime 2022/9/8 16:48
     */
    @GetMapping("/selectOne")
    // @ApiOperation(value = "获取用户信息")

    public TCompany selectOne(@RequestParam("username") String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", username);
        TCompany one = tCompanyService.getOne(queryWrapper);
        return one;
    }

    /**
     * @return code
     * @Description: 通过openid查询企业招聘
     * @Author tangzhuo
     * @CreateTime 2022/9/26 17:24
     */

    @GetMapping("/getWXCompany")
    public TCompany getWXCompany(@RequestParam("code") String code) {
        String openid = this.openid(code);
        if (!"".equals(openid)) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("openid", openid);
            TCompany one = tCompanyService.getOne(queryWrapper);
            return one;
        }
        return null;
    }

    /**
     * @return null
     * @Description: 获取企业用户权限
     * @Author tangzhuo
     * @CreateTime 2022/9/8 16:50
     */

    @GetMapping("/rolePermission")
    public List<TPermission> rolePermission(@RequestParam("companyId") String companyId) {
        loginRecord.CompanyLogin(companyId, 0);
        return tCompanyService.rolePermission(companyId);
    }

    /**
     * @param username 登录名
     * @return null
     * @Description: 获取推荐官信息
     * @Author tangzhuo
     * @CreateTime 2022/9/8 16:50
     */

    @GetMapping("/selectOneRecommend")
    // @ApiOperation(value = "获取用户信息")
    public TRecommend selectOneRecommend(@RequestParam("username") String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", username);
        TRecommend one = tRecommendService.getOne(queryWrapper);
        return one;
    }

    /**
     * @param companyId 推荐官id
     * @return 权限
     * @Description: 获取推荐官权限
     * @Author tangzhuo
     * @CreateTime 2022/9/8 16:51
     */

    @GetMapping("/rolePermissionRecommend")
    public List<TPermission> rolePermissionRecommend(@RequestParam("companyId") String companyId) {
        loginRecord.CompanyLogin(companyId, 1);
        return tRecommendService.rolePermission(companyId);
    }

    /**
     * @param code
     * @return null
     * @Description: 通过openid查询人才推荐挂
     * @Author tangzhuo
     * @CreateTime 2022/9/26 17:25
     */

    @GetMapping("/getWXTRecommend")
    public TRecommend getWXTRecommend(@RequestParam("code") String code) {
        String openid = this.openid(code);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("wx_openid", openid);
        TRecommend one = tRecommendService.getOne(queryWrapper);
        return one;
    }


    public String openid(String code) {
        String access_token = null;
        String openid = null;
        if (code != null) {

            AccessToken access = weixinLoginService.getAccessToken(code);
            if (access != null) {
                // 把获取到的access_token和openId赋值给变量
                access_token = access.getAccess_token();
                openid = access.getOpenid();
                return openid;
            }
        }
        return "";
    }


    /**
     * @param username 登录名
     * @return null
     * @Description: 获取求职者的用户信息
     * @Author tangzhuo
     * @CreateTime 2022/10/8 14:49
     */
    @GetMapping("/getOneTJobhunter")
    public TJobhunter getOneTJobhunter(@RequestParam("username") String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", username);
        TJobhunter one = tJobhunterService.getOne(queryWrapper);
        return one;
    }

    /**
     * @return null
     * @Description: 求职者用户权限
     * @Author tangzhuo
     * @CreateTime 2022/9/8 16:50
     */

    @GetMapping("/roleTJobhunterPermission")
    public List<TPermission> roleTJobhunterPermission(@RequestParam("companyId") String userId) {
        loginRecord.CompanyLogin(userId, 2);
        return tJobhunterService.rolePermission(userId);
    }

    @GetMapping("/getWXTJobhunter")
    public TJobhunter getWXTJobhunter(@RequestParam("code") String code) {
        String openid = this.openid(code);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid", openid);
        TJobhunter one = tJobhunterService.getOne(queryWrapper);
        return one;
    }



    @GetMapping("/getOneAdmin")
    public AdminUser getOneAdmin(@RequestParam("username") String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", username);
        AdminUser one = adminUserService.getOne(queryWrapper);
        return one;
    }

    @GetMapping("/roleAdminPermission")
    public List<TPermission> roleAdminPermission(@RequestParam("userId") String userId) {
      /*  loginRecord.CompanyLogin(userId, 2);*///记录登陆时间
        return adminUserService.rolePermission(userId);
    }



    //获取企业或校园人才推荐管消息

    //用户信息
    @GetMapping("/getTRecommendedCompanies")
    public TRecommendedCompanies getTRecommendedCompanies(@RequestParam("username") String username){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", username);
        TRecommendedCompanies one = tRecommendedCompaniesService.getOne(queryWrapper);
        return one;
    }
    //获取企业推荐官权限
    @GetMapping("/recommendedCompaniesPermission")
    public List<TPermission> recommendedCompaniesPermission(@RequestParam("userId") String userId) {
        return tRecommendedCompaniesService.rolePermission(userId);
    }

    //微信登录
    @GetMapping("/getWXRecommendedCompanies")
    public TRecommendedCompanies getWXRecommendedCompanies(@RequestParam("code") String code) {
        String openid = this.openid(code);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid", openid);
        TRecommendedCompanies one = tRecommendedCompaniesService.getOne(queryWrapper);
        return one;
    }
    @GetMapping("/cccs")
    public String cc(){
        return "cccc";
    }

}
