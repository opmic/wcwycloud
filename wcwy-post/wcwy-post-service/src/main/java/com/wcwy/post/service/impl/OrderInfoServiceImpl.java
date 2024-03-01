package com.wcwy.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.post.dto.ReferralRevenueDTO;
import com.wcwy.post.entity.OrderInfo;
import com.wcwy.post.enums.OrderStatus;
import com.wcwy.post.pojo.DivideIntoPOJO;
import com.wcwy.post.query.OrderInfoInvoiceQuery;
import com.wcwy.post.query.ReferralRevenueQuery;
import com.wcwy.post.service.OrderInfoService;
import com.wcwy.post.mapper.OrderInfoMapper;
import com.wcwy.post.util.OrderNoUtils;
import com.wcwy.post.vo.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【order_info(岗位订单表)】的数据库操作Service实现
* @createDate 2022-10-12 17:06:43
*/
@Service
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
    implements OrderInfoService{
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private IDGenerator idGenerator;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public OrderInfo createOrderByProductId(Payment payment) {
        //查询订单是否存在
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setOrderId(idGenerator.generateCode("OD"));
        orderInfo.setOrderOn(OrderNoUtils.getOrderNo());
        orderInfo.setTitle(payment.getTitle());
        orderInfo.setPaymentType(payment.getPaymentType());
        orderInfo.setState(1);
        orderInfo.setMoney(payment.getMoney());
        orderInfo.setIdentification(4);
        orderInfo.setCreateTime(LocalDateTime.now());
        //orderInfo.setCreateId();
        int insert = orderInfoMapper.insert(orderInfo);
        return insert >0 ? orderInfo : null;
    }

    @Override
    public OrderInfo getOrderStatus(String orderNo) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_on", orderNo);
        OrderInfo orderInfo = baseMapper.selectOne(queryWrapper);
        if(orderInfo == null){
            return null;
        }
        return orderInfo;
    }

    @Override
    public void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus) {
        log.info("更新订单状态 ===> {}", orderStatus.getType());

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_on", orderNo);
       // queryWrapper.eq("identification",4);//充值无忧币
        //更新用户无忧币

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setState(orderStatus.getState());
        orderInfo.setUpdateTime(LocalDateTime.now());
        int update = baseMapper.update(orderInfo, queryWrapper);
        /*if(update>0){
            ValueOperations valueOperations = redisTemplate.opsForValue();
             valueOperations.setIfAbsent(Sole.UPDATE_ORDER.getKey() + orderNo, orderInfo);
        }*/
    }

    @Override
    public void updateStatusByOrderNo(String orderNo, Integer payerTotal, OrderStatus orderStatus) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_on", orderNo);
        // queryWrapper.eq("identification",4);//充值无忧币
        //更新用户无忧币

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setState(orderStatus.getState());
        orderInfo.setPaymentAmount(new BigDecimal(payerTotal).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_HALF_UP));
        orderInfo.setUpdateTime(LocalDateTime.now());
        int update = baseMapper.update(orderInfo, queryWrapper);
    }

    @Override
    public List<OrderInfo> getNoPayOrderByDuration(int minutes, Integer type) {
        //Java获取当前时间的前几分钟的时间
        Instant instant = Instant.now().minus(Duration.ofMinutes(minutes));

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", OrderStatus.NOTPAY.getState());
        queryWrapper.le("create_time", instant);
        queryWrapper.eq("payment_type", type);
        queryWrapper.eq("deleted",0);
       // queryWrapper.eq("identification", 4);//充值无忧币
        List<OrderInfo> orderInfoList = baseMapper.selectList(queryWrapper);
        return orderInfoList;
    }

    @Override
    public List<OrderInfo> getNoPayOrderByDuration(Integer type) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", OrderStatus.NOTPAY.getState());
        queryWrapper.eq("payment_type", type);
        queryWrapper.eq("deleted",0);
        // queryWrapper.eq("identification", 4);//充值无忧币
        List<OrderInfo> orderInfoList = baseMapper.selectList(queryWrapper);
        return orderInfoList;
    }

    @Override
    public IPage<OrderInfo> selectApplyForInvoice(OrderInfoInvoiceQuery orderInfoInvoiceQuery) {
        return orderInfoMapper.selectApplyForInvoice(orderInfoInvoiceQuery.createPage(),orderInfoInvoiceQuery);
    }

    @Override
    public DivideIntoPOJO divideInto(String identityId, String recommendId, Integer type) {
        return orderInfoMapper.divideInto(identityId,recommendId,type);
    }

    @Override
    public List<Map<String,String>> companys() {
        return orderInfoMapper.companys();
    }

    @Override
    public List<OrderInfo> getResumeDownload(int minutes, Integer type) {
        //Java获取当前时间的前几分钟的时间
        Instant instant = Instant.now().minus(Duration.ofMinutes(minutes));

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", OrderStatus.NOTPAY.getState());
        queryWrapper.le("create_time", instant);
        queryWrapper.eq("identification", type);
        // queryWrapper.eq("identification", 4);//充值无忧币
        List<OrderInfo> orderInfoList = baseMapper.selectList(queryWrapper);
        return orderInfoList;
    }

    @Override
    public Double sumMoney(String userid) {
        return orderInfoMapper.sumMoney(userid);
    }

    @Override
    public List<String> getPostLabel(String userid, String keyword) {
        return orderInfoMapper.getPostLabel(userid,keyword);
    }

    @Override
    public IPage<ReferralRevenueDTO> referralRevenue(ReferralRevenueQuery referralRevenueQuery) {
        return orderInfoMapper.referralRevenue(referralRevenueQuery.createPage(),referralRevenueQuery);
    }

    @Override
    public BigDecimal postServiceCharge(String userid, String postId) {
        return orderInfoMapper.postServiceCharge(userid,postId);
    }
}




