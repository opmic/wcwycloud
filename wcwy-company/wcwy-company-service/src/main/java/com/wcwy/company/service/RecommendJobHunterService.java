package com.wcwy.company.service;

import com.wcwy.company.entity.RecommendJobHunter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【recommend_job_hunter(推荐官求职者权限表)】的数据库操作Service
* @createDate 2023-07-24 09:21:49
*/
public interface RecommendJobHunterService extends IService<RecommendJobHunter> {

    /**
     * 添加权限
     * @param tJobHunterId
     */
    void add(String recommend,String tJobHunterId);

    /**
     * 查看是否下载过求职者
     * @param userid
     * @param jobHunterId
     * @return
     */

    RecommendJobHunter selectById(String userid, String jobHunterId);
}
