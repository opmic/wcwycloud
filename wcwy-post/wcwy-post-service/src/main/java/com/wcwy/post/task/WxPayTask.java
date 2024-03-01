package com.wcwy.post.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.post.asyn.InformAsync;
import com.wcwy.post.entity.OrderInfo;
import com.wcwy.post.enums.OrderStatus;
import com.wcwy.post.enums.PayType;
import com.wcwy.post.service.OrderInfoService;
import com.wcwy.post.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: WxPayTask
 * Description:
 * date: 2022/10/18 13:46
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class WxPayTask {
    @Resource
    private WxPayService wxPayService;
    @Resource
    private OrderInfoService orderInfoService;

    @Resource
   private InformAsync informAsync;
    /**
     * 从第0秒开始每隔30秒执行1次，并且未支付的订单
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void orderConfirm() throws Exception {
        log.info("orderConfirm 被执行......");

        List<OrderInfo> orderInfoList = orderInfoService.getNoPayOrderByDuration(PayType.WXPAY.getState());

        for (OrderInfo orderInfo : orderInfoList) {
            String orderNo = orderInfo.getOrderOn();
            log.warn("超时订单 ===> {}", orderNo);

            //核实订单状态：调用微信支付查单接口
            wxPayService.checkOrderStatus(orderInfo,orderNo);
        }
    }
    @Scheduled(cron = "0 0 12 * * ?")//每天中午12点触发
    public void orderConfirm1() throws Exception {
        log.info("orderConfirm 被执行......");
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", OrderStatus.NOTPAY.getState());
        queryWrapper.eq("payment_type", 2);
        queryWrapper.eq("deleted",0);
        List<OrderInfo> orderInfoList = orderInfoService.list(queryWrapper);

        for (OrderInfo orderInfo : orderInfoList) {
            informAsync.orederClosed(orderInfo.getCreateId(),"充值",orderInfo.getState());
        }
    }
    /**
     * 充值中5天未支付的则关单
     * @throws Exception
     */
    @Scheduled(cron = "0/30 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void orderClose() throws Exception {
        log.info("orderClose 被执行......");
        List<OrderInfo> orderInfoList = orderInfoService.getNoPayOrderByDuration(7170,PayType.WXPAY.getState());
        for (OrderInfo orderInfo : orderInfoList) {
            String orderNo = orderInfo.getOrderOn();
            log.warn("超时订单 ===> {}", orderNo);
            //核实订单状态：调用微信支付查单接口
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
            wxPayService.closeOrder(orderNo);
            informAsync.orederClosed(orderInfo.getCreateId(),"充值",3);
        }
    }


    /**
     * 查询下载简历订单  超时直接关闭
     * @throws Exception
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void orderCloseDownload() throws Exception {
        log.info("orderClose 被执行......");
        List<OrderInfo> orderInfoList = orderInfoService.getResumeDownload(7170,1);
        for (OrderInfo orderInfo : orderInfoList) {
            String orderNo = orderInfo.getOrderOn();
            log.warn("超时订单 ===> {}", orderNo);
            //核实订单状态：调用微信支付查单接口
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
           // wxPayService.closeOrder(orderNo);
            informAsync.orederClosed(orderInfo.getCreateId(),"简历下载",3);
        }
    }
}
