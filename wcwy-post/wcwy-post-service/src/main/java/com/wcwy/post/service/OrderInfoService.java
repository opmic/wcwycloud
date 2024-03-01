package com.wcwy.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.post.dto.ReferralRevenueDTO;
import com.wcwy.post.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.post.enums.OrderStatus;
import com.wcwy.post.pojo.DivideIntoPOJO;
import com.wcwy.post.query.OrderInfoInvoiceQuery;
import com.wcwy.post.query.ReferralRevenueQuery;
import com.wcwy.post.vo.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【order_info(岗位订单表)】的数据库操作Service
* @createDate 2022-10-12 17:06:43
*/
public interface OrderInfoService extends IService<OrderInfo> {

    OrderInfo createOrderByProductId(Payment payment);

    OrderInfo getOrderStatus(String orderNo);

    void updateStatusByOrderNo(String orderNo, OrderStatus success);

    void updateStatusByOrderNo(String orderNo, Integer payerTotal,OrderStatus success);

    List<OrderInfo> getNoPayOrderByDuration(int i, Integer type);
    List<OrderInfo> getNoPayOrderByDuration( Integer type);
    IPage<OrderInfo> selectApplyForInvoice(OrderInfoInvoiceQuery orderInfoInvoiceQuery);

    /**
     * 获取求职者的收益
     * @param identityId 身份id
     * @param recommendId 推荐官id
     *  @param type:查找的类型（1:企业 2:推荐官 3:求职者）
     * @return
     */
    DivideIntoPOJO divideInto(String identityId, String recommendId, Integer type);

    /**
     * 获取有订单未处理的企业
     * @return
     */
    List<Map<String,String>> companys();

    List<OrderInfo> getResumeDownload(int i, Integer type);


    Double sumMoney(String userid);

    /**
     * 查询推荐收益的岗位名称
     * @param userid
     * @param keyword
     * @return
     */
    List<String> getPostLabel(String userid, String keyword);

    /**
     * @Description: 推荐收益
     * @param null
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/8/10 16:25
     */

    IPage<ReferralRevenueDTO> referralRevenue(ReferralRevenueQuery referralRevenueQuery);

    /***
     * 获取该岗位的收益
     * @param userid
     * @param postId
     * @return
     */
    BigDecimal postServiceCharge(String userid, String postId);
}
