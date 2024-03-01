package com.wcwy.post.service;

import com.wcwy.post.entity.TPaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_payment_info(支付说明表)】的数据库操作Service
* @createDate 2022-10-12 17:09:09
*/
public interface TPaymentInfoService extends IService<TPaymentInfo> {

    void createPaymentInfo(String plainText);
}
