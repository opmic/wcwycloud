package com.wcwy.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.QRCode;
import com.wcwy.post.entity.AuditInvoice;
import com.wcwy.post.service.AuditInvoiceService;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: AuditInvoiceController
 * Description:
 * date: 2023/7/26 11:13
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "审核发票接口")
@RestController
@RequestMapping("/auditInvoice")
public class AuditInvoiceController {

    @Autowired
    private AuditInvoiceService auditInvoiceService;

    @GetMapping("/selectPage")
    @ApiOperation("查询审核发票状")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", required = false, value = "条数"),
            @ApiImplicitParam(name = "applyForInvoiceId", required = true, value = "发票id")
    })

    @Log(title = "查询审核发票状", businessType = BusinessType.SELECT)
    public R<AuditInvoice> selectPage(@RequestParam("applyForInvoiceId") String applyForInvoiceId,@RequestParam(value = "pageNo",required = false) Integer pageNo){
       if(pageNo==null ){
           pageNo=1;
       }
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("apply_for_invoice",applyForInvoiceId);
        queryWrapper.orderByDesc("carte_time");
        Page<AuditInvoice> page = auditInvoiceService.page(new Page<>(pageNo, 0),queryWrapper);
        return R.success(page);
    }
}
