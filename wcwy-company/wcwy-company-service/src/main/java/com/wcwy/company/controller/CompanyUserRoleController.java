package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.BirthdayUtils;
import com.wcwy.common.redis.enums.CompanyUserRoleEnums;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.dto.CompanyUserRoleDTO;
import com.wcwy.company.query.CompanyUserRoleQuery;
import com.wcwy.company.service.CompanyUserRoleService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: CompanyUserRoleController
 * Description:
 * date: 2023/4/3 20:13
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/companyUserRole")
@Api(tags = "企业查看用户简历权限接口")
public class CompanyUserRoleController {
    @Autowired
    private CompanyUserRoleService companyUserRoleService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private CompanyMetadata companyMetadata;
    @ApiOperation("获取已下载的求职者")
    @PostMapping("/select")
    @Log(title = "获取已下载的求职者", businessType = BusinessType.SELECT)
    public R<CompanyUserRoleDTO> select(@RequestBody CompanyUserRoleQuery companyUserRoleQuery) {
        if(companyUserRoleQuery.getBirthday() !=null){
            Map<String, Date> birthday = BirthdayUtils.getBirthday(companyUserRoleQuery.getBirthday());
            companyUserRoleQuery.setStartTime(birthday.get("startTime"));
            companyUserRoleQuery.setEndTime(birthday.get("endTime"));
        }
        IPage<CompanyUserRoleDTO> iPage = companyUserRoleService.select(companyUserRoleQuery,companyMetadata.userid());
        List<CompanyUserRoleDTO> records = iPage.getRecords();

        if(!CollectionUtils.isEmpty(records)){
            //获取集合里面的简历id
            List<String> collects = records.stream().map(CompanyUserRoleDTO::getResumeId).collect(Collectors.toList());

            List<CompanyUserRoleDTO> companyUserRoleDTOS= companyUserRoleService.selectWorkEducation(collects);
            records.stream().map(record -> {
                return companyUserRoleDTOS.stream().filter(companyUserRoleDTO -> {        //条件判断
                    return StringUtils.pathEquals(record.getResumeId(),companyUserRoleDTO.getResumeId());
                }).map(companyUserRoleDTO -> {
                    record.setPositionName(companyUserRoleDTO.getPositionName());
                    record.setShcoolName(companyUserRoleDTO.getShcoolName());
                    record.setEducation(companyUserRoleDTO.getEducation());
                    record.setCompanyName(companyUserRoleDTO.getCompanyName());
                    return record;                            //返回的结果
                }).collect(Collectors.toList());
            }).flatMap(List::stream).collect(Collectors.toList());//设置返回结果类型

        }

        return R.success(iPage);
    }

    /**
     * 查看是否存在
     *
     * @param userId:求职者id
     * @param companyId:企业id
     * @return
     */
    @GetMapping("/exist")
    public Boolean exist(@RequestParam("userId") String userId, @RequestParam("companyId") String companyId) {

       /* QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("company_id", companyId);
        queryWrapper.eq("deleted", 0);
        int count = companyUserRoleService.count(queryWrapper);*/
      Boolean download=  companyUserRoleService.isDownload(userId,companyId);
        if (download) {
            redisUtils.sSetAndTime(CompanyUserRoleEnums.COMPANY_USER_ROLE + companyId, 3600, userId);
            return download;
        }
        return download;
    }


}
