package com.wcwy.post.mapper;

import com.wcwy.post.entity.ApplyForInvoice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author Administrator
* @description 针对表【apply_for_invoice(申请发票)】的数据库操作Mapper
* @createDate 2022-12-01 10:35:24
* @Entity com.wcwy.post.entity.ApplyForInvoice
*/
public interface ApplyForInvoiceMapper extends BaseMapper<ApplyForInvoice> {

    Map sumInvoicedMoney(@Param("userid") String userid);
}




