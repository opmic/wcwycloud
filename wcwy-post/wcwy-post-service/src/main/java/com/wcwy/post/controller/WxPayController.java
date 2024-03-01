package com.wcwy.post.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sun.jmx.snmp.Timestamp;
import com.wawy.company.api.TopUpApi;
import com.wcwy.common.base.result.R;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.dto.PrePayDTO;
import com.wcwy.company.vo.TopUp;
import com.wcwy.post.config.CreateSign02;
import com.wcwy.post.config.SignV3Utils;
import com.wcwy.post.config.WxPayConfig;
import com.wcwy.post.service.OrderInfoService;
import com.wcwy.post.service.WxPayService;
import com.wcwy.post.session.CompanyMetadata;
import com.wcwy.post.util.HttpUtils;
import com.wcwy.post.util.WechatPay2ValidatorForRequest;
import com.wcwy.post.vo.Payment;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: WxPayController
 * Description:
 * date: 2022/10/13 10:00
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@CrossOrigin //跨域
@RestController
@RequestMapping("/wx-pay")
@Api(tags = "网站微信支付APIv3")
@Slf4j
public class WxPayController {
    @Autowired
    private CompanyMetadata companyMetadata;
    @Resource
    private WxPayService wxPayService;

    @Resource
    private SignV3Utils signV3Utils;
    @Resource
    private Verifier verifier;
    @Resource
    private TopUpApi topUpApi;
    @Resource
    private CreateSign02 createSign02;
    @Resource
    private WxPayConfig wxPayConfig;
    @Resource
    private OrderInfoService orderInfoService;
/*    @GetMapping("/cc")
    @ApiOperation("测试")
    public R cc(){
        TopUp topUp=new TopUp();
        topUp.setUserId("TR2303171351261-2");
        topUp.setOrder("OD2307311151517-2");
        topUp.setMoney(new BigDecimal(20));
        Boolean aBoolean = topUpApi.updateCurrencyCount(topUp);
        return R.success(aBoolean);
    }*/
    /**
     * Native下单
     *
     * @param payment
     * @return
     * @throws Exception
     */
    @ApiOperation("调用统一下单API，生成无忧币支付二维码")
    @PostMapping("/native")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "调用统一下单API，生成无忧币支付二维码", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R nativePay(@Valid @RequestBody Payment payment) throws Exception {
        log.info("发起支付请求 v3");
        int i = payment.getMoney().compareTo(new BigDecimal(0));
        if(i==-1 || i==0){
            return R.fail("请输入正确金额！");
        }
        if("1".equals(payment.getPaymentType())){
            return R.fail("支付宝暂未开发");
        }
        if("3".equals(payment.getPaymentType())){
            return R.fail("银联支付暂未开发");
        }
        if("2".equals(payment.getPaymentType())){
            //返回支付二维码连接和订单号
            Map<String, Object> map = wxPayService.nativePay(payment);
            if (map == null) {
                return R.fail("创建失败");
            }
            return R.success(map);
        }

        return R.fail("请选择支付宝-微信支付-银联支付！");
    }



    /**
     * 支付通知
     * 微信支付通过支付通知接口将用户支付成功消息通知给商户
     */
    @ApiOperation("无忧币支付通知")
    @PostMapping("/native/notify")
    @Log(title = "无忧币支付通知", businessType = BusinessType.UPDATE)
    public String nativeNotify(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();//应答对象
        try {
            //处理通知参数
            String body = HttpUtils.readData(request);
            Map<String, Object> bodyMap = gson.fromJson(body, HashMap.class);
            String requestId = (String) bodyMap.get("id");
            log.info("支付通知的id ===> {}", requestId);
            //log.info("支付通知的完整数据 ===> {}", body);
            //int a = 9 / 0;

            //签名的验证
            WechatPay2ValidatorForRequest wechatPay2ValidatorForRequest = new WechatPay2ValidatorForRequest(verifier, requestId, body);
            if (!wechatPay2ValidatorForRequest.validate(request)) {

                log.error("通知验签失败");
                //失败应答
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "通知验签失败");
                return gson.toJson(map);
            }
            log.info("通知验签成功");
            //处理订单
            wxPayService.processOrder(bodyMap);

            //成功应答
            response.setStatus(200);
            map.put("code", "SUCCESS");
            map.put("message", "成功");
            return gson.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            //失败应答
            response.setStatus(500);
            map.put("code", "ERROR");
            map.put("message", "失败");
            return gson.toJson(map);
        }
    }


    @ApiOperation("用户取消订单")
    @PostMapping("/cancel/{orderNo}")
    @Log(title = "用户取消订单", businessType = BusinessType.UPDATE)
    public R cancel(@PathVariable String orderNo) throws Exception {

        log.info("取消订单");

        wxPayService.cancelOrder(orderNo);
        return R.success("订单已取消");
    }


    @ApiOperation("申请退款")
    @PostMapping("/refunds/{orderNo}/{reason}")
    @Log(title = "申请退款", businessType = BusinessType.UPDATE)
    public R refunds(@PathVariable String orderNo, @PathVariable String reason) throws Exception {

        log.info("申请退款");
        Boolean refund = wxPayService.refund(orderNo, reason);
        if(refund){
            return R.success("申请成功");
        }
        return R.fail("申请失败");
    }

    @ApiOperation("下载账单")
    @GetMapping("/downloadbill/{billDate}/{type}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "billDate", required = true, value = "日期 格式为 2019-06-11 只能获取前一天的账单"),
            @ApiImplicitParam(name = "type", required = true, value = "tradebill ：申请交易账单 或 fundflowbill：申请资金账单")
    })
    @PreAuthorize("hasAnyAuthority('admin')")
    public R downloadBill(
            @PathVariable String billDate,
            @PathVariable String type) throws Exception {

        log.info("下载账单");
        String result = wxPayService.downloadBill(billDate, type);

        return R.success("result", result);
    }


/*    @Resource
    private TCompanyApi tCompanyApi;

    @GetMapping("/cs")
    @ApiOperation("测试")
    @GlobalTransactional
    public String cs() {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId("OD2210132019261-27");
        boolean b = orderInfoService.removeById(orderInfo);
        String cc = tCompanyApi.cc();
        return cc;
    }*/
    /**
     *
     *  total_fee 下单金额：单位（分）
     *  body  商品描述
     *
     * @return
     */
    @PostMapping("/pay")
    @ApiOperation("测试")
    @Transactional(rollbackFor = Exception.class)
    public R Pay(Integer total_fee, String body) throws Exception {
        //生成随机字符串
        String nonce_str= WxPayConfig.getNonceStr();

        //获取当前系统时间戳
   /*     Timestamp sysTime = new Timestamp(System.currentTimeMillis());
        long timestamp = sysTime.getDateTime();*/
        long timestamp = System.currentTimeMillis() / 1000;
        Payment payment=new Payment();
        payment.setUserId(companyMetadata.userid());
        payment.setPaymentType("2");
        payment.setMoney(new BigDecimal(1));
        payment.setTitle("测试");
        Map<String, Object> map = wxPayService.JSAPIPay(payment);
        //对数据进行二次签名
        String prepayId ="prepay_id="+ (String) map.get("prepay_id");
        PrePayDTO prePayVo = wxPayService.weiXinSecondsSign(prepayId, nonce_str, timestamp);
       // HashMap<String, String> heads = SignV3Utils.getSignMap("POST", "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi", nonce_str);
      //  prePayVo.setSign(heads.get("Authorization"));
        String token = createSign02.getToken1(wxPayConfig.getWxappid(), prepayId,nonce_str,timestamp);
        prePayVo.setSign(token);
        return R.success("请求成功",prePayVo);
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation("测试")
    public HashMap<String, String> test() throws Exception{
        //处理请求参数
        String param = JSON.toJSONString("data");
        //获取签名请求头
        HashMap<String, String> heads = SignV3Utils.getSignMap("POST", "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi", param);
        //请求微信接口
       return heads;
    }


}
