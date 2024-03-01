package com.wcwy.post.service;

import com.wcwy.post.entity.ApplyForInvoice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author Administrator
* @description 针对表【apply_for_invoice(申请发票)】的数据库操作Service
* @createDate 2022-12-01 10:35:24
*/
public interface ApplyForInvoiceService extends IService<ApplyForInvoice> {

    /**
     * 查看已开发票金额
     * @param userid
     * @return
     */
    Map sumInvoicedMoney(String userid);
}
