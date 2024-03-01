package com.wawy.company.api;

import com.wcwy.company.entity.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * ClassName: TCompanyLoginApi
 * Description:
 * date: 2022/9/1 13:55
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-company")
public interface TCompanyLoginApi {

    /**
     * @Description: 查询招聘企业
     * @param username：用户名
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/9/26 16:54
     */
    @GetMapping("/login/selectOne")
    TCompany selectOne(@RequestParam("username") String username);

    /**
     * @Description: 查询权限
     * @param companyId  用户id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/9/8 16:49
     */

    @GetMapping("/login/rolePermission")
    List<TPermission> rolePermission(@RequestParam("companyId") String companyId);

    /**
     * @Description: 查询人才推荐官
     * @param username：用户名
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/9/26 16:54
     */

    @GetMapping("/login/selectOneRecommend")
    TRecommend selectOneRecommend(@RequestParam("username") String username);

    /**
     * @Description: 查询权限
     * @param permissionId  人才推荐官id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/9/8 16:49
     */
    @GetMapping("/login/rolePermissionRecommend")
    List<TPermission> rolePermissionRecommend(@RequestParam("companyId") String permissionId);


    @GetMapping("/login/getWXCompany")
     TCompany getWXCompany(@RequestParam("code") String code);

    @GetMapping("/login/getWXTRecommend")
     TRecommend  getWXTRecommend(@RequestParam("code") String code);

    @GetMapping("/login/getOpenid")
    public String  openid(@RequestParam("code")String code ,@RequestParam("state")String state);


    @GetMapping("/login/getOneTJobhunter")
    public TJobhunter getOneTJobhunter(@RequestParam("username") String username);

    @GetMapping("/login/roleTJobhunterPermission")
    public List<TPermission> roleTJobhunterPermission(@RequestParam("companyId") String userId) ;

    @GetMapping("/login/getWXTJobhunter")
    public TJobhunter getWXTJobhunter(@RequestParam("code") String code);

    @GetMapping("/login/getOneAdmin")
    public AdminUser getOneAdmin(@RequestParam("username") String username) ;

    @GetMapping("/login/roleAdminPermission")
    public List<TPermission> roleAdminPermission(@RequestParam("userId") String userId) ;


    //获取企业或校园人才推荐管消息

    //用户信息
    @GetMapping("/login/getTRecommendedCompanies")
    public TRecommendedCompanies getTRecommendedCompanies(@RequestParam("username") String username);

    //获取企业推荐官权限
    @GetMapping("/login/recommendedCompaniesPermission")
    public List<TPermission> recommendedCompaniesPermission(@RequestParam("userId") String userId);

    //微信登录
    @GetMapping("/login/getWXRecommendedCompanies")
    public TRecommendedCompanies getWXRecommendedCompanies(@RequestParam("code") String code);
    @GetMapping("/jobhunter/insertCodeJobHunter")
    TJobhunter insertCodeJobHunter(@RequestParam("phone") String phone,@RequestParam(value = "code",required = false) String code,@RequestParam(value = "ip",required = false) String ip);
}
