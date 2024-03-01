package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.entity.TJobhunterHideCompany;
import com.wcwy.company.service.TCompanyService;
import com.wcwy.company.service.TJobhunterHideCompanyService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: TJobHunterHideCompanyController
 * Description:
 * date: 2023/8/3 14:42
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/jobHunterHideCompany")
@Api(tags = "屏蔽企业接口")
public class TJobHunterHideCompanyController {
    @Autowired
    private CompanyMetadata companyMetadata;

    @Autowired
    private TJobhunterHideCompanyService tJobhunterHideCompanyService;

    @Autowired
    private TCompanyService tCompanyService;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IDGenerator idGenerator;
    @GetMapping("/add")
    @ApiOperation("添加屏蔽公司")
    @ApiImplicitParam(name = "company", required = true, value = "屏蔽的公司名称")
    @Log(title = "添加屏蔽公司", businessType = BusinessType.INSERT)
    public R add(@RequestParam("company") List<String> company){
        List<String> list = tCompanyService.selectCompanyName(company,companyMetadata.userid());
        if(list.size()>0){
            String fail="";
            for (String s : list) {
                fail=fail+s;
            }
            return R.fail("您所投的"+fail+"公司岗位未完成交互,暂时不能屏蔽！");
        }
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("resume_id",companyMetadata.userid());
        int count = tJobhunterHideCompanyService.count(queryWrapper);
        if(count>5){
            return R.fail("最多可以添加5家屏蔽公司！");
        }
        for (String s : company) {
            QueryWrapper queryWrapper1=new QueryWrapper();
            queryWrapper1.eq("resume_id",companyMetadata.userid());
            queryWrapper1.eq("company_name",s);
            if(tJobhunterHideCompanyService.count(queryWrapper1)>0){
                continue;
            }
            TJobhunterHideCompany tJobhunterHideCompany=new TJobhunterHideCompany();
            tJobhunterHideCompany.setCompanyName(s);
            tJobhunterHideCompany.setId(idGenerator.generateCode("JH"));
            tJobhunterHideCompany.setCreateTime(LocalDateTime.now());
            tJobhunterHideCompany.setResumeId(companyMetadata.userid());
            boolean save = tJobhunterHideCompanyService.save(tJobhunterHideCompany);

        }
            //清除缓存
            redisUtils.del(Cache.JOB_HUNTER_HIDE_COMPANY.getKey()+companyMetadata.userid());

        return R.success();
    }



    @GetMapping("/cancel")
    @ApiOperation("取消屏蔽")
    @ApiImplicitParam(name = "company", required = true, value = "屏蔽的公司名称")
    @Log(title = "取消屏蔽", businessType = BusinessType.DELETE)
    public R cancel(@RequestParam("company") String company){
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("resume_id",companyMetadata.userid());
        updateWrapper.eq("company_name",company);
        boolean remove = tJobhunterHideCompanyService.remove(updateWrapper);
        //清除缓存
        redisUtils.del(Cache.JOB_HUNTER_HIDE_COMPANY.getKey()+companyMetadata.userid());
        if(remove){
           return R.success("已取消屏蔽!");
        }

        return R.fail("取消失败!");
    }

    @GetMapping("/select")
    @ApiOperation("查询屏蔽的公司")
    public R<String> select(){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("resume_id",companyMetadata.userid());
        List<TJobhunterHideCompany> list = tJobhunterHideCompanyService.list(queryWrapper);
        List list1=new ArrayList(10);
        for (TJobhunterHideCompany tJobhunterHideCompany : list) {
            list1.add(tJobhunterHideCompany.getCompanyName());
        }
        return R.success(list1);
    }


    /**
     * @Description: 企业查询
     * @param jobHunter:求职者id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/8/8 14:30
     */

    @GetMapping("/cacheCompany")
    public List<Object> cacheCompany(@RequestParam("jobHunter") String jobHunter ){
        List<Object> objects = tJobhunterHideCompanyService.cacheCompany(jobHunter);
       return objects;
    }
}
