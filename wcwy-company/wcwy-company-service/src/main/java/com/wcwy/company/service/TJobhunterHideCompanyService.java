package com.wcwy.company.service;

import com.wcwy.company.entity.TJobhunterHideCompany;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_jobhunter_hide_company(用户屏蔽公司表)】的数据库操作Service
* @createDate 2022-10-08 11:58:44
*/
public interface TJobhunterHideCompanyService extends IService<TJobhunterHideCompany> {


    /**
     * @Description: 查看屏蔽的企业
     * @param  JobHunter:求职者id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/8/7 13:41
     */

   List<Object>  cacheCompany(String JobHunter);
}
