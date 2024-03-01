package com.wcwy.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.post.entity.Invoice;
import com.wcwy.post.service.InvoiceService;
import com.wcwy.post.mapper.InvoiceMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【invoice(发票抬头信息)】的数据库操作Service实现
* @createDate 2022-11-21 08:22:20
*/
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice>
    implements InvoiceService{

}




