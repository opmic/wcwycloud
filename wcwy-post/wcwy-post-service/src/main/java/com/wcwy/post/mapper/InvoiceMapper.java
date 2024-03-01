package com.wcwy.post.mapper;

import com.wcwy.post.entity.Invoice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【invoice(发票抬头信息)】的数据库操作Mapper
* @createDate 2022-11-21 08:22:20
* @Entity com.wcwy.post.entity.Invoice
*/
@Mapper
public interface InvoiceMapper extends BaseMapper<Invoice> {

}




