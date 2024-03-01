package com.wcwy.post.service;

import com.wcwy.company.dto.PrePayDTO;
import com.wcwy.post.entity.OrderInfo;
import com.wcwy.post.vo.Payment;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.Map;

public interface WxPayService {
    /**
     * @Description: 下单处理
     * @param payment
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/10/18 14:43
     */

    Map<String, Object> nativePay(Payment payment) throws Exception;

    /**
     * @Description: 订单处理
     * @param bodyMap
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/10/18 14:43
     */

    void processOrder(Map<String, Object> bodyMap) throws Exception;

    /**
     * @Description: //核实订单状态：调用微信支付查单接口
     * @param orderInfo
     * @param orderNo
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/10/18 14:42
     */

    void checkOrderStatus(OrderInfo orderInfo, String orderNo) throws Exception;
    /**
     * @Description: 查询订单
     * @param orderNo
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/10/18 14:42
     */

    String queryOrder(String orderNo) throws IOException;

    /**
     * @Description: 取消订单
     * @param orderNo
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/10/18 14:41
     */

    void cancelOrder(String orderNo) throws Exception;

    /**
     * 船舰退款申请
     * @param orderNo
     * @param reason
     */
    Boolean refund(String orderNo, String reason);

    String downloadBill(String billDate, String type) throws Exception;

    String queryBill(String billDate, String type) throws Exception;


    /**
     * 创建订单
     * @param orderInfo
     * @return
     */
    Map<String, Object> createQRCode(OrderInfo orderInfo) throws Exception;

     void closeOrder(String orderNo) throws Exception;

    /**
     * APP的秘钥  未完善  使用前请注意
     * @param orderNo
     * @param nonce_str
     * @param timestamp
     * @return
     */
    PrePayDTO SecondsSign(String orderNo, String nonce_str, long timestamp);

     Map<String, Object> JSAPIPay(Payment payment) throws IOException;

    /**
     * 微信小程序
     * @param orderNo
     * @param nonce_str
     * @param timestamp
     * @return
     */
    PrePayDTO weiXinSecondsSign(String prepayId, String nonce_str, long timestamp) throws Exception;
}
