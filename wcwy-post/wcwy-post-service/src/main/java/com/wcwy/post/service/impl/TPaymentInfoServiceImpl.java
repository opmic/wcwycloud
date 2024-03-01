package com.wcwy.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.wcwy.post.entity.TPaymentInfo;
import com.wcwy.post.enums.PayType;
import com.wcwy.post.service.TPaymentInfoService;
import com.wcwy.post.mapper.TPaymentInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_payment_info(支付说明表)】的数据库操作Service实现
* @createDate 2022-10-12 17:09:09
*/
@Service
@Slf4j
public class TPaymentInfoServiceImpl extends ServiceImpl<TPaymentInfoMapper, TPaymentInfo>
    implements TPaymentInfoService{


    @Override
    public void createPaymentInfo(String plainText) {
        log.info("记录支付日志");
        Gson gson = new Gson();
        HashMap plainTextMap = gson.fromJson(plainText, HashMap.class);

        //订单号
        String orderNo = (String)plainTextMap.get("out_trade_no");
        //业务编号
        String transactionId = (String)plainTextMap.get("transaction_id");
        //支付类型
        String tradeType = (String)plainTextMap.get("trade_type");
        //交易状态
        String tradeState = (String)plainTextMap.get("trade_state");
        //用户实际支付金额
        Map<String, Object> amount = (Map)plainTextMap.get("amount");
        Integer payerTotal = ((Double) amount.get("payer_total")).intValue();

        TPaymentInfo paymentInfo = new TPaymentInfo();
        paymentInfo.setOrderNo(orderNo);
        paymentInfo.setPaymentType(PayType.WXPAY.getType());
        paymentInfo.setTransactionId(transactionId);
        paymentInfo.setTradeType(tradeType);
        paymentInfo.setTradeState(tradeState);
        paymentInfo.setPayerTotal(payerTotal);
        paymentInfo.setContent(plainText);

        baseMapper.insert(paymentInfo);
    }
}




