package com.wcwy.post.service;

import com.wcwy.post.entity.ResumePaymentConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【resume_payment_config】的数据库操作Service
* @createDate 2023-09-07 09:21:32
*/
public interface ResumePaymentConfigService extends IService<ResumePaymentConfig> {

    List<ResumePaymentConfig> selectList();

    /**
     * 获取校园简历价格
     * @return
     */
    ResumePaymentConfig campusRecruitment();

    /**
     * 获取职场简历价格
     * @return
     */
    ResumePaymentConfig jobMarket();
}
