package com.wcwy.post.asyn;


import com.alibaba.fastjson2.JSON;
import com.wcwy.common.base.utils.MessageUtil;
import com.wcwy.common.redis.util.RedisUtils;

import com.wcwy.company.vo.MessageVO;
import com.wcwy.post.produce.MessageProduce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


/**
 * ClassName: InformAsync
 * Description:通知公告
 * date: 2024/1/3 16:53
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class InformAsync {

    @Autowired
    private MessageProduce messageProduce;


    @Autowired
    private RedisUtils redisUtils;

    /**
     *
     * @param userId 用户id
     * @param state 订单状态
     * @param title 订单类型
     * @param postName 岗位名称
     * @param orderId 订单id
     * @param payTime:支付时间
     * @param identification 职位类型
     */

    @Async
    public void orderPayment(String userId , Integer state, String title, String postName, String orderId, LocalDate payTime, Integer identification){
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy002OrderPayment = MessageUtil.SY002_ORDER_PAYMENT;
        sy002OrderPayment.put("identity",MessageUtil.identity(userId));
        int putid = MessageUtil.identity(userId);
        sy002OrderPayment.put("identity",putid);
        if(putid==0){
            sy002OrderPayment.put("router","/AccountOrder");
        }else if(putid==1){
            sy002OrderPayment.put("router","/TAccountOrder");
        }else if(putid==2){
           // sy002OrderPayment.put("router","/Interview");
        }
        sy002OrderPayment.put("chatId",userId);
        Map map=new HashMap();
        map.put("state",state);
        map.put("title",title);
        map.put("postName",postName);
        map.put("orderId",orderId);
        map.put("identification",identification);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        map.put("payTime",payTime.format(formatter));
        sy002OrderPayment.put("content",JSON.toJSONString(map));
        messageVO.setMessage(sy002OrderPayment);

        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
    }
    @Async
    public void  orederClosed(String userId,String title,int state){
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TcSendAResume = MessageUtil.SY002_ORDER_PAYMENT;
        sy004TcSendAResume.put("chatId",userId);
        Map map=new HashMap();
        map.put("state",state);
        map.put("title",title);
        sy004TcSendAResume.put("content", com.alibaba.fastjson.JSON.toJSONString(map));
        messageVO.setMessage(sy004TcSendAResume);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
    }
}
