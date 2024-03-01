package com.wcwy.post.mapper;

import com.wcwy.post.entity.AuditInvoice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【audit_invoice(发票审核)】的数据库操作Mapper
* @createDate 2023-07-26 11:12:19
* @Entity com.wcwy.post.entity.AuditInvoice
*/
@Mapper
public interface AuditInvoiceMapper extends BaseMapper<AuditInvoice> {

}




