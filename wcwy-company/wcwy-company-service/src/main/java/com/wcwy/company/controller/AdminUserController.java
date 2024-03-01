package com.wcwy.company.controller;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.base.result.R;
import com.wcwy.common.config.session.LoginUser;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.dto.AdminUserRoles;
import com.wcwy.company.entity.AdminUser;
import com.wcwy.company.entity.TCompany;
import com.wcwy.company.entity.TPermission;
import com.wcwy.company.service.AdminUserService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.entity.SysLogininfor;
import com.wcwy.system.enums.BusinessType;
import com.wcwy.system.service.SysLogininforService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: AdminUserController
 * Description:
 * date: 2022/11/18 14:40
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/adminUser")
@Api(tags = "管理员账号接口")
public class AdminUserController {


    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/info")
    @ApiOperation("根据token获取管理员信息")
    public R<AdminUserRoles> info(){
        String userid = companyMetadata.userid();
        List<TPermission> tPermissions = adminUserService.rolePermission(userid);
        AdminUser byId = adminUserService.getById(userid);
        AdminUserRoles adminUserRoles=new AdminUserRoles();
        BeanUtils.copyProperties(byId,adminUserRoles);
        List list=new ArrayList();
        for (TPermission tPermission : tPermissions) {
            list.add(tPermission.getCode());
        }
        adminUserRoles.setRoles(list);
        return R.success(adminUserRoles);
    }

    @GetMapping("/test")
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @ApiOperation("测试")
    public R test(){
        int i=1/0;
        return R.success();
    }

    @PostConstruct
    public void init() {
        redisUtils.set("system","admin");
        LoginUser loginUser=new LoginUser();
        loginUser.setId("system123");
        loginUser.setUsername("system");
        redisUtils.set("systemUser", JSON.toJSONString(loginUser));
    }

}
