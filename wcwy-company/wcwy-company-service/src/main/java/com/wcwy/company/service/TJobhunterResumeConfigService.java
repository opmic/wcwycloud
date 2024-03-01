package com.wcwy.company.service;

import com.wcwy.company.entity.TJobhunterResumeConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_jobhunter_resume_config(隐藏简历配置表)】的数据库操作Service
* @createDate 2022-10-08 11:58:56
*/
public interface TJobhunterResumeConfigService extends IService<TJobhunterResumeConfig> {

    void updateRedis(String userId);
}
