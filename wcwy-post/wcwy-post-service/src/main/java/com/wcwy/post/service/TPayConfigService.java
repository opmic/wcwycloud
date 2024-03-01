package com.wcwy.post.service;

import com.wcwy.post.entity.TPayConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_pay_config(支付兑换配置表)】的数据库操作Service
* @createDate 2023-07-14 15:42:45
*/
public interface TPayConfigService extends IService<TPayConfig> {

    TPayConfig selectOne();

}
