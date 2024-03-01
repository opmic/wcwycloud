package com.wcwy.company.service;

import com.wcwy.company.entity.EiCompanyPost;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【ei_company_post(岗位基础信息)】的数据库操作Service
* @createDate 2023-03-30 14:41:39
*/
public interface EiCompanyPostService extends IService<EiCompanyPost> {

    /**
     * @Description: 获取岗位基本信息
     * @param putInPost ：岗位id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/4/13 16:42
     */


    EiCompanyPost add(String putInPost);



}
