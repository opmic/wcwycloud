package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.dto.CompanyGlanceOverDTO;
import com.wcwy.company.entity.CompanyGlanceOver;
import com.wcwy.company.service.CompanyGlanceOverService;
import com.wcwy.company.vo.SelectCompanyGlanceOver;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author Administrator
 * @description 针对表【company_glance_over(企业浏览表)】的数据库操作Service
 * @createDate 2022-11-01 09:29:51
 */
@RestController
@RequestMapping("/companyGlanceOver")
@Api(tags = "企业浏览接口")
public class CompanyGlanceOverController {
    @Resource
    private CompanyGlanceOverService companyGlanceOverService;
    @Autowired
    private IDGenerator idGenerator;

    @GetMapping("/addCompanyGlanceOver/{glanceOverUserId}/{companyId}")
    @ApiOperation("添加浏览")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "glanceOverUserId", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "companyId", required = true, value = "企业id")
    })
    @Log(title = "添加浏览", businessType = BusinessType.INSERT)
    @AutoIdempotent
    public R addCompanyGlanceOver(@PathVariable("glanceOverUserId") String glanceOverUserId, @PathVariable("companyId") String companyId) {
        if(StringUtils.isEmpty(glanceOverUserId) || StringUtils.isEmpty(glanceOverUserId)){
            return R.fail("数据不能为空");
        }
        CompanyGlanceOver companyGlanceOver = new CompanyGlanceOver();
        companyGlanceOver.setGlanceOverUserId(glanceOverUserId);
        companyGlanceOver.setCompanyId(companyId);
        companyGlanceOver.setGlanceOverTime(LocalDateTime.now());
        companyGlanceOver.setDeleted(0);
        boolean save = companyGlanceOverService.save(companyGlanceOver);
        if (save) {
            return R.success("添加成功!");
        }
        return R.fail("添加失败!");
    }


    @PostMapping("/selectPage")
    @ApiOperation("查看企业浏览简历接口")
    @Log(title = "查看企业浏览简历接口", businessType = BusinessType.SELECT)
    public R<CompanyGlanceOverDTO> selectPage(@RequestBody SelectCompanyGlanceOver selectCompanyGlanceOver) {
      IPage<CompanyGlanceOverDTO> iPage=  companyGlanceOverService.seletcPage(selectCompanyGlanceOver);
        return R.success(iPage);
    }

    @GetMapping("/deleteCompanyGlanceOver/{glanceOverId}")
    @ApiOperation("删除企业浏览简历接口")
    @ApiImplicitParam(name = "glanceOverId", required = true, value = "浏览表id")
    @Log(title = "删除企业浏览简历接口", businessType = BusinessType.DELETE)
    @AutoIdempotent
    public R deleteCompanyGlanceOver(@PathVariable("glanceOverId")String glanceOverId){
        if(StringUtils.isEmpty(glanceOverId)){
            return R.fail("浏览表id不能为空");
        }
        boolean b = companyGlanceOverService.removeById(glanceOverId);
        if(b){
            return R.success("删除成功!");
        }
        return R.fail("删除失败!");
    }
}
