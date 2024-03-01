package com.wcwy.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.post.service.CompanyUserRoleService;
import com.wcwy.post.service.WxPayService;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: CompanyUserRoleController
 * Description:
 * date: 2022/10/17 13:48
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "企业查看用户简历权限接口")
@RestController
@RequestMapping("/companyUserRole")
public class CompanyUserRoleController {
    @Autowired
    private CompanyUserRoleService companyUserRoleService;

    @Autowired
    private WxPayService wxPayService;

    @GetMapping("/selectExamineRole")
    @ApiOperation("查看是否是否有权限返回下载地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", required = true, value = "企业id"),
            @ApiImplicitParam(name = "jobhunterId", required = true, value = "求职者id")
    })
    @Log(title = "查看是否是否有权限返回下载地址", businessType = BusinessType.SELECT)
   // @PreAuthorize("hasAnyAuthority('admin')")
    public R<Boolean> selectExamineRole(@RequestParam("companyId") String companyId, @RequestParam("jobhunterId") String jobhunterId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("company_id", companyId);
        queryWrapper.eq("user_id", jobhunterId);
        int count = companyUserRoleService.count(queryWrapper);
        if (count > 0) {
            return R.success("有权限查看", true);
        }
        return R.success("无权限查看", false);
    }



}
