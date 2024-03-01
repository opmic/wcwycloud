package com.wcwy.post.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.wawy.company.api.TopUpApi;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.base.utils.WechatPaySignUtil;
import com.wcwy.common.redis.enums.Lock;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.dto.PrePayDTO;
import com.wcwy.company.vo.TopUp;
import com.wcwy.post.config.SignV3Utils;
import com.wcwy.post.config.WxPayConfig;
import com.wcwy.post.entity.OrderInfo;
import com.wcwy.post.entity.RunningWater;
import com.wcwy.post.entity.TRefundInfo;
import com.wcwy.post.enums.OrderStatus;
import com.wcwy.post.enums.wxpay.WxApiType;
import com.wcwy.post.enums.wxpay.WxNotifyType;
import com.wcwy.post.enums.wxpay.WxTradeState;
import com.wcwy.post.produce.CompanyUserRoleProduce;
import com.wcwy.post.service.*;
import com.wcwy.post.session.CompanyMetadata;
import com.wcwy.post.util.OrderNoUtils;
import com.wcwy.post.vo.Payment;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.asn1.ocsp.Signature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class WxPayServiceImpl implements WxPayService {

    @Resource
    public OrderInfoService orderInfoService;
    @Autowired
    private IDGenerator idGenerator;
    @Resource
    private WxPayConfig wxPayConfig;
    @Resource
    private CloseableHttpClient wxPayClient;
    @Resource
    private CompanyMetadata companyMetadata;
    @Resource
    private TRefundInfoService tRefundInfoService;
    @Resource
    private TopUpApi topUpApi;
    @Resource
    private TPaymentInfoService tPaymentInfoService;
    private final ReentrantLock lock = new ReentrantLock();
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RunningWaterService runningWaterService;
    @Autowired
    private CompanyUserRoleProduce companyUserRoleProduce;
    @Resource(description = "wxPayNoSignClient")
    private CloseableHttpClient wxPayNoSignClient; //无需应答签名
    @Resource
    private TPostShareService tPostShareService;

    @Override
    public Map<String, Object> nativePay(Payment payment) throws IOException {
        String userid = companyMetadata.userid();
        payment.setUserId(userid);
        log.info("生成订单");
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(idGenerator.generateCode("OD"));
        orderInfo.setOrderOn(OrderNoUtils.getOrderNo());
        orderInfo.setTitle(payment.getTitle());
        orderInfo.setPaymentType(payment.getPaymentType());
        orderInfo.setState(1);
        orderInfo.setMoney(payment.getMoney());
        orderInfo.setIdentification(4);
        orderInfo.setCreateTime(LocalDateTime.now());
        orderInfo.setCreateId(payment.getUserId());
        orderInfo.setCreateName(companyMetadata.userName());
        orderInfo.setPayer(companyMetadata.userid());
        LocalDate currentDate = LocalDate.now();
        orderInfo.setNoPaymentTime(currentDate.plusDays(5));
        orderInfo.setPayerTime(LocalDateTime.now());
        /*  orderInfo.setPaymentAmount(new BigDecimal(0));*/
        log.info("调用统一下单API");
        String codeUrl = orderInfo.getCodeUrl();
        //调用统一下单API
        HttpPost httpPost = new HttpPost(wxPayConfig.getDomain().concat(WxApiType.NATIVE_PAY.getType()));
        // 请求body参数
        Gson gson = new Gson();
        Map paramsMap = new HashMap();
        paramsMap.put("appid", wxPayConfig.getAppid());
        paramsMap.put("mchid", wxPayConfig.getMchId());
        paramsMap.put("description", orderInfo.getTitle());
        paramsMap.put("out_trade_no", orderInfo.getOrderOn());
        paramsMap.put("time_expire", DateUtils.getPastDate(5));
        paramsMap.put("notify_url", wxPayConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));
        Map amountMap = new HashMap();
        System.out.println(orderInfo.getMoney().multiply(new BigDecimal("100")));
        int money = orderInfo.getMoney().multiply(new BigDecimal("100")).intValue();
        amountMap.put("total", money);
        amountMap.put("currency", "CNY");

        paramsMap.put("amount", amountMap);

        //将参数转换成json字符串
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数 ===> {}" + jsonParams);

        StringEntity entity = new StringEntity(jsonParams, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpPost);
        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());//响应体
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功, 返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            } else {
                log.info("Native下单失败,响应码 = " + statusCode + ",返回结果 = " + bodyAsString);
                throw new IOException("request failed");
            }

            //响应结果
            Map<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
            //二维码
            codeUrl = resultMap.get("code_url");

            //保存二维码
            orderInfo.setCodeUrl(codeUrl);
            boolean save = orderInfoService.save(orderInfo);
            if (!save) {
                return null;
            }

            //返回二维码
            Map<String, Object> map = new HashMap<>();
            map.put("codeUrl", codeUrl);
            map.put("orderNo", orderInfo.getOrderOn());
            map.put("createTime", orderInfo.getCreateTime());
            map.put("title", orderInfo.getTitle());
            map.put("orderId", orderInfo.getOrderId());
            return map;

        } finally {
            response.close();
        }
    }


    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void processOrder(Map<String, Object> bodyMap) throws Exception {
        log.info("处理订单");
        //解密报文
        String plainText = decryptFromResource(bodyMap);
        //将明文转换成map
        Gson gson = new Gson();
        HashMap plainTextMap = gson.fromJson(plainText, HashMap.class);

        String orderNo = (String) plainTextMap.get("out_trade_no");
        Map<String, Object> amount = (Map) plainTextMap.get("amount");
        Integer payerTotal = ((Double) amount.get("payer_total")).intValue();
         /*在对业务数据进行状态检查和处理之前，
        要采用数据锁进行并发控制，
        以避免函数重入造成的数据混乱*/
        //尝试获取锁：
        // 成功获取则立即返回true，获取失败则立即返回false。不必一直等待锁的释放
        RLock lock = redissonClient.getLock(Lock.UPDATE_CURRENCY_COUNT.getLock() + orderNo);
        boolean tryLock = lock.tryLock(10, 30, TimeUnit.SECONDS);
        if (tryLock) {
            try {

                //处理重复的通知
                //接口调用的幂等性：无论接口被调用多少次，产生的结果是一致的。
                OrderInfo orderStatus = orderInfoService.getOrderStatus(orderNo);
                if (!OrderStatus.NOTPAY.getState().equals(orderStatus.getState())) {
                    return;
                }
                //查询订单是否支付成功过 保证数据一致性
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("order_on", orderNo);
                OrderInfo one = orderInfoService.getOne(queryWrapper);
                if (one.getState() == 2) {
                    return;
                }
                //更新订单状态
                orderInfoService.updateStatusByOrderNo(orderNo, payerTotal, OrderStatus.SUCCESS);
                if (orderStatus.getIdentification() == 4) {
                    TopUp topUp = new TopUp();
                    topUp.setOrder(orderStatus.getOrderId());
                    topUp.setMoney(orderStatus.getMoney());
                    topUp.setUserId(orderStatus.getCreateId());
                    Boolean aBoolean = topUpApi.updateCurrencyCount(topUp);
                    if (!aBoolean) {
                        log.error("更新无忧币失败");
                        throw new Exception("更新无忧币失败");
                    }
                } else if (orderStatus.getIdentification() == 1) {
                    //直接下载简历
                    //1.添加无忧币流水
                    //2.开放权限
                    companyUserRoleProduce.sendAsyncMessage(orderStatus);
                    //更新发布岗位纪录表
                    tPostShareService.updateShare(one.getPostId());
                }


                //记录支付日志
                tPaymentInfoService.createPaymentInfo(plainText);
            } finally {
                //要主动释放锁
                lock.unlock();
            }
        }
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void checkOrderStatus(OrderInfo orderInfo, String orderNo) throws Exception {
        log.warn("根据订单号核实订单状态 ===> {}", orderNo);
        RLock lock = redissonClient.getLock(Lock.UPDATE_CURRENCY_COUNT.getLock() + orderNo);
        boolean tryLock = lock.tryLock(10, 30, TimeUnit.SECONDS);
        if (tryLock) {
            try {
                //调用微信支付查单接口
                String result = this.queryOrder(orderNo);
                Gson gson = new Gson();
                Map<String, String> resultMap = gson.fromJson(result, HashMap.class);
                //获取微信支付端的订单状态
                String tradeState = resultMap.get("trade_state");
                //判断订单状态
                if (WxTradeState.SUCCESS.getType().equals(tradeState)) {
                    //查询订单是否支付成功过 保证数据一致性
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("order_on", orderNo);
                    OrderInfo one = orderInfoService.getOne(queryWrapper);
                    if (one.getState() == 2) {
                        return;
                    }
                    log.warn("核实订单已支付 ===> {}", orderNo);
                    //如果确认订单已支付则更新本地订单状态
                    orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);
                    //记录支付日志
                    tPaymentInfoService.createPaymentInfo(result);

                    if (one.getIdentification() == 4) {
                        //更新无忧币
                        TopUp topUp = new TopUp();
                        topUp.setOrder(orderInfo.getOrderId());
                        topUp.setMoney(orderInfo.getMoney());
                        topUp.setUserId(orderInfo.getCreateId());
                        Boolean aBoolean = topUpApi.updateCurrencyCount(topUp);
                        if (!aBoolean) {
                            log.error("定时任务更新无忧币失败");
                            throw new Exception("定时任务更新无忧币失败");

                        }
                       /* RunningWater runningWater = new RunningWater();
                        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
                        runningWater.setDeleted(0);
                        runningWater.setIfIncome(2);
                        runningWater.setMoney(topUp.getMoney());
                        runningWater.setSource(1);
                        runningWater.setOrderId(topUp.getOrder());
                        runningWater.setUserId(topUp.getUserId());
                        boolean save = runningWaterService.save(runningWater);
                        if (!save) {
                            throw new Exception("更新无忧币流水失败");
                        }*/
                        log.error("定时任务更新无忧币成功");
                    } else if (one.getIdentification() == 1) {
                        //直接下载简历
                        //1.添加无忧币流水及分成
                        //2.开放权限
                        companyUserRoleProduce.sendAsyncMessage(one);
                    }

                }
                if (WxTradeState.REFUND.getType().equals(tradeState)) {
                    log.warn("核实订单已支付 ===> {}", orderNo);
                    //如果确认订单已支付则更新本地订单状态
                    orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_SUCCESS);

                    //处理更新无忧币及退款记录
                }

                if (WxTradeState.NOTPAY.getType().equals(tradeState)) {
                    log.info("核实订单未支付 ===> {}", orderNo);

                    //如果订单未支付，则调用关单接口
                    //  this.closeOrder(orderNo);

                    //更新本地订单状态
                    //  orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
                }
                if (WxTradeState.CLOSED.getType().equals(tradeState)) {
                    log.warn("核实订单已关闭 ===> {}", orderNo);
                    //更新本地订单状态
                    orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
                }
            } finally {
                //要主动释放锁
                lock.unlock();
            }
        }


    }

    @Override
    public String queryOrder(String orderNo) throws IOException {
        log.info("查单接口调用 ===> {}", orderNo);

        String url = String.format(WxApiType.ORDER_QUERY_BY_NO.getType(), orderNo);
        url = wxPayConfig.getDomain().concat(url).concat("?mchid=").concat(wxPayConfig.getMchId());

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpGet);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());//响应体
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功, 返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            } else if (statusCode == 404) { //处理成功，无返回Body
                updatePaymentType(orderNo);
            } else {
                log.info("查单接口调用,响应码 = " + statusCode + ",返回结果 = " + bodyAsString);
                throw new IOException("request failed");
            }

            return bodyAsString;

        } finally {
            response.close();
        }
    }

    void updatePaymentType(String orderNo) {
        UpdateWrapper queryWrapper=new UpdateWrapper();
        queryWrapper.eq("order_on",orderNo);
        queryWrapper.set("payment_type",OrderStatus.ERROR_CANCEL.getState());
        boolean update = orderInfoService.update(queryWrapper);
    }

    @Override
    public void cancelOrder(String orderNo) throws Exception {
        //调用微信支付的关单接口
        this.closeOrder(orderNo);
        //更新商户端的订单状态
        orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CANCEL);
    }

    @Override
    public Boolean refund(String orderNo, String reason) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_on", orderNo);
        OrderInfo one = orderInfoService.getOne(queryWrapper);
        TRefundInfo tRefundInfo = new TRefundInfo();
        tRefundInfo.setId(idGenerator.generateCode("RF"));
        tRefundInfo.setOrderNo(orderNo);
        tRefundInfo.setTotalFee(one.getMoney());
        tRefundInfo.setReason(reason);
        tRefundInfo.setRefundNo(OrderNoUtils.getRefundNo());
        tRefundInfo.setRefundState(1);
        tRefundInfo.setCreateTime(LocalDateTime.now());
        boolean save = tRefundInfoService.save(tRefundInfo);
        if (save) {
            //更新订单状态
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("order_on", orderNo);
            updateWrapper.set("state", 5);
            updateWrapper.set("update_time", LocalDateTime.now());
            updateWrapper.set("update_id", companyMetadata.userid());
            boolean update = orderInfoService.update(updateWrapper);
            if (update) {
                return true;
            }
            tRefundInfoService.removeById(tRefundInfo);
        }
        return false;
    }

    @Override
    public String downloadBill(String billDate, String type) throws Exception {
        log.warn("下载账单接口调用 {}, {}", billDate, type);

        //获取账单url地址
        String downloadUrl = this.queryBill(billDate, type);
        //创建远程Get 请求对象
        HttpGet httpGet = new HttpGet(downloadUrl);
        httpGet.addHeader("Accept", "application/json");

        //使用wxPayClient发送请求得到响应
        CloseableHttpResponse response = wxPayNoSignClient.execute(httpGet);

        try {

            String bodyAsString = EntityUtils.toString(response.getEntity());

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("成功, 下载账单返回结果 = " + bodyAsString);
            } else if (statusCode == 204) {
                log.info("成功");
            } else {
                throw new RuntimeException("下载账单异常, 响应码 = " + statusCode + ", 下载账单返回结果 = " + bodyAsString);
            }

            return bodyAsString;

        } finally {
            response.close();
        }
    }

    @Override
    public String queryBill(String billDate, String type) throws Exception {
        log.warn("申请账单接口调用 {}", billDate);

        String url = "";
        if ("tradebill".equals(type)) {
            url = WxApiType.TRADE_BILLS.getType();
        } else if ("fundflowbill".equals(type)) {
            url = WxApiType.FUND_FLOW_BILLS.getType();
        } else {
            throw new RuntimeException("不支持的账单类型");
        }

        url = wxPayConfig.getDomain().concat(url).concat("?bill_date=").concat(billDate);

        //创建远程Get 请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", "application/json");

        //使用wxPayClient发送请求得到响应
        CloseableHttpResponse response = wxPayClient.execute(httpGet);

        try {

            String bodyAsString = EntityUtils.toString(response.getEntity());

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("成功, 申请账单返回结果 = " + bodyAsString);
            } else if (statusCode == 204) {
                log.info("成功");
            } else {
                throw new RuntimeException("申请账单异常, 响应码 = " + statusCode + ", 申请账单返回结果 = " + bodyAsString);
            }

            //获取账单下载地址
            Gson gson = new Gson();
            Map<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
            return resultMap.get("download_url");

        } finally {
            response.close();
        }
    }

    @Override
    public Map<String, Object> createQRCode(OrderInfo orderInfo) throws Exception {
/*        //调用统一下单API
        HttpPost httpPost = new HttpPost(wxPayConfig.getDomain().concat(WxApiType.NATIVE_PAY.getType()));
        // 请求body参数
        Gson gson = new Gson();
        Map paramsMap = new HashMap();
        paramsMap.put("appid", wxPayConfig.getAppid());
        paramsMap.put("mchid", wxPayConfig.getMchId());
        paramsMap.put("description", orderInfo.getTitle());
        paramsMap.put("out_trade_no", orderInfo.getOrderOn());
        paramsMap.put("notify_url", wxPayConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));
        Map amountMap = new HashMap();
        System.out.println(orderInfo.getMoney().multiply(new BigDecimal("100")));
        int money = orderInfo.getMoney().multiply(new BigDecimal("100")).intValue();
        amountMap.put("total", money);
        amountMap.put("currency", "CNY");

        paramsMap.put("amount", amountMap);

        //将参数转换成json字符串
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数 ===> {}" + jsonParams);

        StringEntity entity = new StringEntity(jsonParams, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpPost);*/
        try {
         /*   String bodyAsString = EntityUtils.toString(response.getEntity());//响应体
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功, 返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            } else {
                log.info("Native下单失败,响应码 = " + statusCode + ",返回结果 = " + bodyAsString);
                throw new IOException("request failed");
            }
            String codeUrl = orderInfo.getCodeUrl();
            //响应结果
            Map<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
            //二维码
            codeUrl = resultMap.get("code_url");

            //保存二维码
            orderInfo.setCodeUrl(codeUrl);*/
            boolean save = orderInfoService.save(orderInfo);
            if (!save) {
                return null;
            }

            //返回二维码
            Map<String, Object> map = new HashMap<>(10);
            /*  map.put("codeUrl", codeUrl);*/
            map.put("orderNo", orderInfo.getOrderOn());
            map.put("createTime", orderInfo.getCreateTime());
            map.put("title", orderInfo.getTitle());
            map.put("orderId", orderInfo.getOrderId());
            map.put("money", orderInfo.getMoney());
            return map;

        } finally {
            /*response.close();*/
        }
    }

   /* @Override
    public String queryOrder(String orderNo) {
        try {
            log.info("查单接口调用 ===> {}", orderNo);

            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);
            request.setBizContent(bizContent.toString());

            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                log.info("调用成功，返回结果 ===> " + response.getBody());
                return response.getBody();
            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
                //throw new RuntimeException("查单接口的调用失败");
                return null;//订单不存在
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("查单接口的调用失败");
        }
    }*/


    /**
     * 对称解密
     *
     * @param bodyMap
     * @return
     */
    private String decryptFromResource(Map<String, Object> bodyMap) throws GeneralSecurityException {

        log.info("密文解密");

        //通知数据
        Map<String, String> resourceMap = (Map) bodyMap.get("resource");
        //数据密文
        String ciphertext = resourceMap.get("ciphertext");
        //随机串
        String nonce = resourceMap.get("nonce");
        //附加数据
        String associatedData = resourceMap.get("associated_data");

        log.info("密文 ===> {}", ciphertext);
        AesUtil aesUtil = new AesUtil(wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        String plainText = aesUtil.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                ciphertext);

        log.info("明文 ===> {}", plainText);

        return plainText;
    }

    /**
     * 关单接口的调用
     *
     * @param orderNo
     */
    @Override
    public void closeOrder(String orderNo) throws Exception {

        log.info("关单接口的调用，订单号 ===> {}", orderNo);

        //创建远程请求对象
        String url = String.format(WxApiType.CLOSE_ORDER_BY_NO.getType(), orderNo);
        url = wxPayConfig.getDomain().concat(url);
        HttpPost httpPost = new HttpPost(url);

        //组装json请求体
        Gson gson = new Gson();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mchid", wxPayConfig.getMchId());
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数 ===> {}", jsonParams);

        //将请求参数设置到请求对象中
        StringEntity entity = new StringEntity(jsonParams, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpPost);

        try {
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功200");
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功204");
            } else {
                log.info("Native下单失败,响应码 = " + statusCode);
                throw new IOException("request failed");
            }

        } finally {
            response.close();
        }
    }


    /**
     * 通过prePayId.进行二次签名
     */
    @Override
    public PrePayDTO SecondsSign(String prePayId, String nonce_str, long  timestamp){

        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", wxPayConfig.getWxappid());
        params.put("partnerid",wxPayConfig.getMchId());
        params.put("prepayid",prePayId);
        params.put("package","Sign=WXPay");
        params.put("noncestr", nonce_str);
        params.put("timestamp", String.valueOf(timestamp));
        //生成签名
        String key = wxPayConfig.getApiV3Key();//api2秘钥
        String sign = WechatPaySignUtil.sign(params, key);
//        params.put("sign", sign);
        //封装参数
        PrePayDTO prePayVo = new PrePayDTO();
        prePayVo.setAppid(wxPayConfig.getWxappid());
        prePayVo.setPartnerid( wxPayConfig.getMchId());
        prePayVo.setPrepayId(prePayId);
        prePayVo.setNoncestr(nonce_str);
        prePayVo.setTimestamp(timestamp);
        prePayVo.setSignType("RSA");
        prePayVo.setSign(sign);
        return prePayVo;
    }

    @Override
    public Map<String, Object> JSAPIPay(Payment payment) throws IOException {
        String userid = companyMetadata.userid();
        payment.setUserId(userid);
        log.info("生成订单");
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(idGenerator.generateCode("OD"));
        orderInfo.setOrderOn(OrderNoUtils.getOrderNo());
        orderInfo.setTitle(payment.getTitle());
        orderInfo.setPaymentType(payment.getPaymentType());
        orderInfo.setState(1);
        orderInfo.setMoney(payment.getMoney());
        orderInfo.setIdentification(4);
        orderInfo.setCreateTime(LocalDateTime.now());
        orderInfo.setCreateId(payment.getUserId());
        orderInfo.setCreateName(companyMetadata.userName());
        orderInfo.setPayer(companyMetadata.userid());
        orderInfo.setPayerTime(LocalDateTime.now());
        /*  orderInfo.setPaymentAmount(new BigDecimal(0));*/
        log.info("调用统一下单API");
        String codeUrl = orderInfo.getCodeUrl();

        //调用统一下单API
        HttpPost httpPost = new HttpPost(wxPayConfig.getDomain().concat(WxApiType.JSAPI_PAY.getType()));
        // 请求body参数
        Gson gson = new Gson();
        Map paramsMap = new HashMap();
        paramsMap.put("appid",wxPayConfig.getWxappid());
        paramsMap.put("mchid", wxPayConfig.getMchId());
        paramsMap.put("description", orderInfo.getTitle());
        paramsMap.put("out_trade_no", orderInfo.getOrderOn());
        paramsMap.put("time_expire", DateUtils.getPastDate(5));
        paramsMap.put("notify_url", wxPayConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));
        Map amountMap = new HashMap();
       // System.out.println(orderInfo.getMoney().multiply(new BigDecimal("100")));
        int money = orderInfo.getMoney().multiply(new BigDecimal("100")).intValue();
        amountMap.put("total", money);
        amountMap.put("currency", "CNY");

        paramsMap.put("amount", amountMap);
        Map payer = new HashMap();
        payer.put("openid","oilbh5I1-oX0taLK2QXbcxaem_PY");
        paramsMap.put("payer", payer);

       // paramsMap.put("out_trade_no", orderInfo.getOrderOn());
        //将参数转换成json字符串
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数 ===> {}" + jsonParams);

        StringEntity entity = new StringEntity(jsonParams, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpPost);
        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());//响应体
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功, 返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            } else {
                log.info("Native下单失败,响应码 = " + statusCode + ",返回结果 = " + bodyAsString);
                throw new IOException("request failed");
            }

            //响应结果
            Map<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
            //二维码
            codeUrl = resultMap.get("prepay_id");

            //保存二维码
            orderInfo.setCodeUrl(codeUrl);
            boolean save = orderInfoService.save(orderInfo);
            if (!save) {
                return null;
            }

            //返回二维码
            Map<String, Object> map = new HashMap<>();
            map.put("prepay_id", codeUrl);
            map.put("orderNo", orderInfo.getOrderOn());
            map.put("createTime", orderInfo.getCreateTime());
            map.put("title", orderInfo.getTitle());
            map.put("orderId", orderInfo.getOrderId());
            return map;

        } finally {
            response.close();
        }
    }

    @Override
    public PrePayDTO weiXinSecondsSign(String prePayId, String nonce_str, long timestamp) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", wxPayConfig.getWxappid());
        params.put("nonceStr",nonce_str);
        params.put("package","prepay_id="+prePayId);
        params.put("signType","RSA");
        params.put("timeStamp", String.valueOf(timestamp));
        //生成签名
        String key ="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC6LkwgxqJ5SwmB\n" +
                "ZLFFCb7DKrK2UK3OQjNQLygsW97lE4xZcRBu93VA8mT5oz7TCfcUL5alQ4ML7C9V\n" +
                "Whhyd1Fjto4SyXMJTVWTDH/UodbXEaRRpSBkN6/Kd9KsQ4JJx2XwtrBUhwx9FxJk\n" +
                "U5jFsXnh3cDe3OngkAHG98W0Bs2f8IX3YMxUT7K59+8XbQPYFVd5syNrGTrKlqBI\n" +
                "SmqDH7bxEGobMBdCAh7BF+C1x6sB3G//rHfPFo8DzcqhncV+jsaDmAxvxPHKMah7\n" +
                "dTP0kvWOXvPFQtDhfQw3xPwyp9uFqhOPHqkFljwLxOEk8l0hQYwgzBI+yx7rLThU\n" +
                "XqOkSeCNAgMBAAECggEAeqgVr2ItmAs/b+eOghUxgBKZOlNcrg4fECyBIvhDmFeQ\n" +
                "lzxNv014ChmnPG+37AEcrkfLz4TyyOAJLwn0ITVKgY67etUM3Iv+XKpXbRZSG86b\n" +
                "ms5W0K9/OLxIJwUyyLh9CGDTOlzedWrFyyLn+xY5XfxiHHEeA7vb7+E8dqYkwcDs\n" +
                "B6KmYuQuqub7wssD82yj7G0S4uOu+7Rw2fMylHZ4o+DkZNa828fvC4SP3APN2hBU\n" +
                "c2sZro3RwXcoXVl2mgWwGkZ7Adfug2Vx66OvDAVQsjGOJ/uhKiXMvfz3fcvbmndn\n" +
                "OmrLnbNuEvQGoCcHB44pVRIYueNcID/vyvKoWJKBAQKBgQDkxuS7v2x3xQ9XUvjE\n" +
                "1rQI55MwBM2ItuaNuMyCqgQ/kDyiLixP41uXpd9SsqLm7k8HpbRUaB4MQBQPrfMB\n" +
                "yUVl12HGbQAoijMIgAEp9L+GpFg71lJkvL1DN+2m/6LsWsF6CGQ4866AYTuMEcTZ\n" +
                "3Y/j1mgjCYFuG1DS6MgQv/Er6QKBgQDQVdKkow4Z5pj1oNatuZv8gQrxE1evbJsU\n" +
                "a4NVbA0EJc+Co3lPQKTZz3KkHO0FKwr5PUZHZQdnNbQTJuJ5ySLNJ2fnwg03Il9b\n" +
                "t/ggS4Ebk3I/rf3jBpZhzvlnusE86+YTX3DtVu1O0LVLR15r+5x/JXdxV797ACv9\n" +
                "YYSHiUG9BQKBgQDibXg12mAgqolkhFpzd4z4wzqKbDaA+YV2/1BqgptxzfA1FD4H\n" +
                "U59zmFhQIT3aEkNl7jtszx/uP/2bBy9ctThac7HyEi/179JSt15viC2HtWEe2CD0\n" +
                "U1l/DfvJLXqzM6AKiAOp2oT7y0CEgZGzj/a6KZsoEmBn+eEk3gAlk9O/AQKBgFS2\n" +
                "4T2TFBPSIdaXfVQNCnHFo0ZeICS0G+dUxIXCtxQ9r8CmapigZ6gt46b6ICMe2op4\n" +
                "sRAs87KzMrMq96Kf+CfF40lpLeiCcJYiG6I+MZSeAzIDtR5QumuxNtdIKHV2UwsD\n" +
                "ny7TTxHiaiXfMnTkTkGhYY1UKgeBDWIt+i8G0BVlAoGAVAmBdW4LrC3AeljFkPdl\n" +
                "zrSvQjEkxGNGa+8mQfhAFZGbOc7DH6/0h8V8yLYHeUK3XmGq2+ONao878ce+5G67\n" +
                "mWUxZTkZEu2+WuZmUhjir6WAIZVXHKUz3DTc/92uYfGMP3QThBoESn08zB0g+Rxb\n" +
                "GrH5/JAPSZwwJTDoNlO1kdM=";//api2秘钥
       // String sign = WechatPaySignUtil.sign(params, key);
        String sign = SignV3Utils.sign1(params,key);
//        params.put("sign", sign);
        //封装参数
        PrePayDTO prePayVo = new PrePayDTO();
        prePayVo.setAppid(wxPayConfig.getWxappid());
        prePayVo.setPartnerid( wxPayConfig.getMchId());
        prePayVo.setPrepayId(prePayId);
        prePayVo.setNoncestr(nonce_str);
        prePayVo.setTimestamp(timestamp);
        prePayVo.setSignType("RSA");
        prePayVo.setSign(sign);
        return prePayVo;
    }


}
