package com.wcwy.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.post.entity.ApplyForInvoice;
import com.wcwy.post.service.ApplyForInvoiceService;
import com.wcwy.post.mapper.ApplyForInvoiceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Administrator
 * @description 针对表【apply_for_invoice(申请发票)】的数据库操作Service实现
 * @createDate 2022-12-01 10:35:24
 */
@Service
public class ApplyForInvoiceServiceImpl extends ServiceImpl<ApplyForInvoiceMapper, ApplyForInvoice>
        implements ApplyForInvoiceService {
    @Resource
    private ApplyForInvoiceMapper applyForInvoiceMapper;

    @Override
    public Map sumInvoicedMoney(String userid) {
        return applyForInvoiceMapper.sumInvoicedMoney(userid);
    }
}




