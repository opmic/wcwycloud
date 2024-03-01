package com.wcwy.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.post.entity.Invoice;
import com.wcwy.post.service.InvoiceService;
import com.wcwy.post.session.CompanyMetadata;
import com.wcwy.post.vo.InvoiceVO;
import com.wcwy.post.vo.UpdateInvoiceVO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【invoice(发票抬头信息)】的数据库操作Service
 * @createDate 2022-11-19 08:53:53
 */
@Api(tags = "发票抬头信息")
@RequestMapping("/invoice")
@RestController
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private IDGenerator idGenerator;

    @ApiOperation("企业添加发票抬头")
    @PostMapping("/addInvoice")
    @Transactional
    @Log(title = "企业添加发票抬头", businessType = BusinessType.INSERT)
    @AutoIdempotent
    public R addInvoice(@Valid @RequestBody InvoiceVO invoiceVO) {
    /*    if (invoiceVO.getTacitlyApprove() == 1) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("binding_user", companyMetadata.userid());
            updateWrapper.set("tacitly_approve", 0);
            invoiceService.update(updateWrapper);
        }*/
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("create_user",companyMetadata.userid());

        List<Invoice> list = invoiceService.list(queryWrapper);
        if(list.size()>0){
            return R.fail("已存在发票抬头,不能进行添加!");
        }
        if(invoiceVO.getInvoiceType()==2){
            if(StringUtils.isEmpty(invoiceVO.getBankOfDeposit())){
                return R.fail("基本开户银行不能为空!");
            }
            if(StringUtils.isEmpty(invoiceVO.getAccountNumber())){
                return R.fail("基本开户银行账号不能为空!");
            }
            if(StringUtils.isEmpty(invoiceVO.getAddress())){
                return R.fail("企业注册地址不能为空!");
            }
            if(StringUtils.isEmpty(invoiceVO.getPhone())){
                return R.fail("企业注册电话不能为空!");
            }
        }
        Invoice invoice = new Invoice();
        BeanUtils.copyProperties(invoiceVO, invoice);
        invoice.setInvoiceId(idGenerator.generateCode("IV"));
        invoice.setBindingUser(companyMetadata.userid());
        invoice.setDeleted(0);
        boolean save = invoiceService.save(invoice);
        if (save) {
            return R.success("添加成功!");
        }
        return R.fail("添加失败!");
    }

    @ApiOperation("删除发票抬头")
    @GetMapping("/deleteInvoice/{invoiceId}")
    @ApiImplicitParam(name = "invoiceId", required = true, value = "发票抬头id")
    @Log(title = "删除发票抬头", businessType = BusinessType.DELETE)
    @AutoIdempotent
    public R deleteInvoice(@PathVariable("invoiceId") String invoiceId) {
        if (StringUtils.isEmpty(invoiceId)) {
            return R.fail("发票抬头id不能为空!");
        }
        String userid = companyMetadata.userid();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("invoice_id", invoiceId);
        queryWrapper.eq("binding_user", userid);
        boolean remove = invoiceService.remove(queryWrapper);
        if (remove) {
            return R.success("删除成功!");
        }
        return R.fail("删除失败!");
    }

    @GetMapping("/selectInvoice")
    @ApiOperation("查询发票抬头")
    @Log(title = "查询发票抬头", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "invoiceId", required = false, value = "发票抬头id")
    public R<Invoice> selectInvoice(@RequestParam(value = "invoiceId", required = false) String invoiceId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(invoiceId)) {
            queryWrapper.eq("invoice_id", invoiceId);
        }
        queryWrapper.eq("binding_user", companyMetadata.userid());
        queryWrapper.eq("deleted",0);
        queryWrapper.orderByDesc("tacitly_approve");
        List<Invoice> list = invoiceService.list(queryWrapper);
        return R.success(list);
    }

 /*   @GetMapping("/asdasd")
    @ApiOperation("sadsadas")
    public R<Invoice> asdsadsads() {

        return R.success(null);
    }*/

    @PostMapping("/updateInvoice")
    @ApiOperation("修改发票抬头")
    @Log(title = "修改发票抬头", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R updateInvoice(@RequestBody UpdateInvoiceVO updateInvoiceVO) {
  /*      if (updateInvoiceVO.getTacitlyApprove() == 1) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("binding_user", companyMetadata.userid());
            updateWrapper.set("tacitly_approve", 0);
            invoiceService.update(updateWrapper);
        }*/
        if(updateInvoiceVO.getInvoiceType()==2){
            if(StringUtils.isEmpty(updateInvoiceVO.getBankOfDeposit())){
                return R.fail("基本开户银行不能为空!");
            }
            if(StringUtils.isEmpty(updateInvoiceVO.getAccountNumber())){
                return R.fail("基本开户银行账号不能为空!");
            }
            if(StringUtils.isEmpty(updateInvoiceVO.getAddress())){
                return R.fail("企业注册地址不能为空!");
            }
            if(StringUtils.isEmpty(updateInvoiceVO.getPhone())){
                return R.fail("企业注册电话不能为空!");
            }
        }
        Invoice invoice=new Invoice();
        BeanUtils.copyProperties(updateInvoiceVO,invoice);
        boolean b = invoiceService.updateById(invoice);
        if(b){
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }


}
