package com.wcwy.post.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.post.entity.ApplyForInvoice;
import com.wcwy.post.entity.OrderInfo;
import com.wcwy.post.query.SelectApplyForInvoiceQuery;
import com.wcwy.post.service.ApplyForInvoiceService;
import com.wcwy.post.service.OrderInfoService;
import com.wcwy.post.session.CompanyMetadata;
import com.wcwy.post.vo.ApplyForInvoiceVO;
import com.wcwy.post.vo.UpdateApplyForInvoiceVO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: ApplyForInvoiceController
 * Description:申请发票接口
 * date: 2022/11/21 14:11
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "申请发票接口")
@RestController
@RequestMapping("/applyForInvoice")
public class ApplyForInvoiceController {
    @Autowired
    private ApplyForInvoiceService applyForInvoiceService;

    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private RedisTemplate redisTemplate;

    // @Autowired
    // private RabbitTemplate rabbitTemplate;
    @PostMapping("/saveApplyForInvoice")
    @ApiOperation("申请发票")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "申请发票", businessType = BusinessType.INSERT)
    @AutoIdempotent
    public R addApplyForInvoice(@Valid @RequestBody ApplyForInvoiceVO applyForInvoiceVO) {
        // SetOperations setOperations = redisTemplate.opsForSet();
        List<String> bindingOrder = applyForInvoiceVO.getBindingOrder();
        BigDecimal sumMoney=new BigDecimal(0);
        for (String s : bindingOrder) {
            // Boolean aBoolean = setOperations.isMember(Sole.UPDATE_ORDER_INVOICE.getKey(), s);
            OrderInfo byId = orderInfoService.getById(s);
            if (byId == null) {
                return R.fail("订单" + s + "不存在!");
            }
            if( byId.getIdentification() !=4){
                return R.fail("非充值订单不能开发票！");
            }
            if( byId.getState() !=2){
                return R.fail("非交易成功订单不能开发票！");
            }
            if (!StringUtils.isEmpty(byId.getInvoice())) {
                return R.fail("订单" + s + "已开发票!");
            }
            sumMoney=  sumMoney.add(byId.getPaymentAmount());
        }
        applyForInvoiceVO.setMoney(sumMoney);
        ApplyForInvoice applyForInvoice = new ApplyForInvoice();
        BeanUtils.copyProperties(applyForInvoiceVO, applyForInvoice);
        applyForInvoice.setApplyForInvoiceId(idGenerator.generateCode("AF"));
        applyForInvoice.setCreateUser(companyMetadata.userid());
        boolean save = applyForInvoiceService.save(applyForInvoice);
        if (save) {

            /* Map map=new ConcurrentHashMap();*/
            /* map.put("applyForInvoiceId",applyForInvoice.getApplyForInvoiceId());*/
          /*  map.put("msg",bindingOrder);
            String s = JSONObject.toJSONString(map);*/
            //  rabbitTemplate.convertAndSend("apply_forInvoice_exchange","invoice",s.getBytes(StandardCharsets.UTF_8),new CorrelationData(UUID.randomUUID().toString()));
            LambdaUpdateWrapper<OrderInfo> updateWrapper = new LambdaUpdateWrapper();
            for (String s : bindingOrder) {
                updateWrapper.eq(OrderInfo::getOrderId, s).or();
            }

            updateWrapper.set(OrderInfo::getInvoice, applyForInvoice.getApplyForInvoiceId());
            boolean update = orderInfoService.update(updateWrapper);
            if (update) {
                return R.success("已发送申请,等待客服处理!");
            }
            //申请失败则删除
            //setOperations.remove(Sole.UPDATE_ORDER_INVOICE.getKey(), bindingOrder);

        }
        return R.fail("申请失败!");
    }


    @GetMapping("/cancelApplyForInvoice/{invoiceId}")
    @ApiOperation("取消申请发票")
    @ApiImplicitParam(name = "invoiceId", required = true, value = "发票id")
    @Transactional
    @Log(title = "取消申请发票", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R cancelApplyForInvoice(@PathVariable("invoiceId") String invoiceId) {
        //    SetOperations setOperations = redisTemplate.opsForSet();
        if (StringUtils.isEmpty(invoiceId)) {
            return R.fail("发票id不能为空!");
        }
        ApplyForInvoice byId = applyForInvoiceService.getById(invoiceId);
        if(byId==null){
            return R.fail("未查到该发票申请记录！ ");
        }
        if (byId.getProcessState() == 1) {
            return R.fail("您已经申请成功,不能取消");
        }
        //取消发票
        if (!byId.getCreateUser().equals(companyMetadata.userid())) {
            return R.fail("操作异常！");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("apply_for_invoice_id", invoiceId);
        updateWrapper.set("process_state", 3);
        updateWrapper.set("update_time", LocalDateTime.now());
        updateWrapper.set("update_user", companyMetadata.userid());
        boolean update = applyForInvoiceService.update(updateWrapper);
        if (update) {
            ApplyForInvoice byId1 = applyForInvoiceService.getById(invoiceId);
            List<String> bindingOrder = byId1.getBindingOrder();
            for (String s : bindingOrder) {
                UpdateWrapper updateWrapperOrder = new UpdateWrapper();
                updateWrapperOrder.eq("order_id", s);
                updateWrapperOrder.set("invoice", "");
                boolean updateOrder = orderInfoService.update(updateWrapperOrder);
                if (updateOrder) {
                    // setOperations.remove(Sole.UPDATE_ORDER_INVOICE.getKey(), s);
                }
            }
            return R.success("已取消申请!");
        }
        return R.fail("取消申请失败!");
    }

    @PostMapping("/updateApplyForInvoice")
    @ApiOperation("修改申请发票")
    @Log(title = "修改申请发票", businessType = BusinessType.UPDATE)
    public R UpdateApplyForInvoice(@Valid @RequestBody UpdateApplyForInvoiceVO updateApplyForInvoiceVO) {
        ApplyForInvoice byId = applyForInvoiceService.getById(updateApplyForInvoiceVO.getApplyForInvoiceId());
        if (byId == null) {
            return R.fail("没有该发票申请!");
        }
        if (byId.getProcessState() == 1) {
            return R.fail("该发票已经申请成功,不允许修改!");
        }
        ApplyForInvoice applyForInvoice = new ApplyForInvoice();
        BeanUtils.copyProperties(updateApplyForInvoiceVO, applyForInvoice);
        applyForInvoice.setUpdateTime(LocalDateTime.now());
        applyForInvoice.setUpdateUser(companyMetadata.userid());
        applyForInvoice.setProcessState(0);
        boolean b = applyForInvoiceService.updateById(applyForInvoice);
        if (b) {
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }

    @PostMapping("/selectApplyForInvoice")
    @ApiOperation("查询发票")
    @Log(title = "查询发票", businessType = BusinessType.SELECT)
    public R<ApplyForInvoice> selectApplyForInvoice(@RequestBody SelectApplyForInvoiceQuery selectApplyForInvoiceQuery) {
        String userid = companyMetadata.userid();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("create_user", userid);
        if (!StringUtils.isEmpty(selectApplyForInvoiceQuery.getApplyForInvoiceId())) {
            queryWrapper.eq("apply_for_invoice_id", selectApplyForInvoiceQuery.getApplyForInvoiceId());
        }
        if (!StringUtils.isEmpty(selectApplyForInvoiceQuery.getNumber())) {
            queryWrapper.eq("number", selectApplyForInvoiceQuery.getNumber());
        }

        if (!StringUtils.isEmpty(selectApplyForInvoiceQuery.getType())) {
            queryWrapper.eq("type", selectApplyForInvoiceQuery.getType());
        }
        if (!StringUtils.isEmpty(selectApplyForInvoiceQuery.getInvoiceType())) {
            queryWrapper.eq("invoice_type", selectApplyForInvoiceQuery.getInvoiceType());
        }


        if (selectApplyForInvoiceQuery.getProcessState() != null) {
            queryWrapper.eq("process_state", selectApplyForInvoiceQuery.getProcessState());
        }
        if (selectApplyForInvoiceQuery.getEndTime() != null || selectApplyForInvoiceQuery.getBeginTime() != null) {
            if(selectApplyForInvoiceQuery.getEndTime() != null && selectApplyForInvoiceQuery.getBeginTime() != null){
                queryWrapper.between("create_time",selectApplyForInvoiceQuery.getBeginTime(),selectApplyForInvoiceQuery.getEndTime());
            }
           if(selectApplyForInvoiceQuery.getEndTime() != null && selectApplyForInvoiceQuery.getBeginTime() == null){
               queryWrapper.eq("create_time",selectApplyForInvoiceQuery.getEndTime());

           }
            if(selectApplyForInvoiceQuery.getEndTime() == null && selectApplyForInvoiceQuery.getBeginTime() != null){
                queryWrapper.ge("create_time",selectApplyForInvoiceQuery.getBeginTime());

            }
        }

        queryWrapper.orderByDesc("create_time");
        IPage<ApplyForInvoice> page = applyForInvoiceService.page(selectApplyForInvoiceQuery.createPage(), queryWrapper);
       Map d= applyForInvoiceService.sumInvoicedMoney(companyMetadata.userid());
        Map map=new HashMap(2);
        map.put("IPage",page);
        if(d.get("money")==null){
            d.put("money",0);
        }
        map.put("sumInvoicedMoney",d);
        return R.success(map);
    }



}
