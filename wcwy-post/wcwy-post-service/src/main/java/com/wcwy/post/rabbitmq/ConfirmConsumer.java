/*
package com.wcwy.post.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.base.utils.DivideIntoUtil;
import com.wcwy.common.redis.enums.Record;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.dto.InviteEntryPutInResume;
import com.wcwy.post.entity.HeadhunterPositionRecord;
import com.wcwy.post.entity.OrderInfo;
import com.wcwy.post.entity.TPostShare;
import com.wcwy.post.service.HeadhunterPositionRecordService;
import com.wcwy.post.service.OrderInfoService;
import com.wcwy.post.service.TCompanyPostService;
import com.wcwy.post.service.TPostShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

*/
/**
 * ClassName: ConfirmConsumer
 * Description:
 * date: 2022/9/19 14:22
 *
 * @author tangzhuo
 * @since JDK 1.8
 *//*

@Component
@Slf4j
public class ConfirmConsumer {
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    @Resource
    private IDGenerator idGenerator;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private TPostShareService tPostShareService;
    @Autowired
    private TCompanyPostService tCompanyPostService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private HeadhunterPositionRecordService headhunterPositionRecordService;
    @RabbitListener(queues = CONFIRM_QUEUE_NAME)
    public void receiveMsg(Message message) {
        String msg = new String(message.getBody());
         log.info("接受到队列 confirm.queue 消息:{}", msg);
          InviteEntryPutInResume inviteEntry = JSON.parseObject(msg, InviteEntryPutInResume.class);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //保证只被消费一次
        Boolean aBoolean = valueOperations.setIfAbsent(Sole.INSERT_ORDER.getKey()+inviteEntry.getInviteEntryId(), inviteEntry.getInviteEntryId());
        if(! aBoolean){
            return;
        }
        this.updateRedisPost(inviteEntry.getPutInPost(),8);
        //赏金岗位
        if (inviteEntry.getPostType() == 1) {
            Map<String, BigDecimal> stringBigDecimalMap = DivideIntoUtil.moneyReward(inviteEntry.getHiredBounty(), inviteEntry.getPutInUser(), inviteEntry.getSharePerson());
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(idGenerator.generateCode("OD"));
            orderInfo.setTitle("入职付职位");
            orderInfo.setPostId(inviteEntry.getPutInPost());
            orderInfo.setPutInResumeId(inviteEntry.getPutInResumeId());
            orderInfo.setState(1);
            String s = tCompanyPostService.selectCompany(inviteEntry.getPutInPost());
            orderInfo.setCreateId(s);
            orderInfo.setJobhunterId(inviteEntry.getPutInJobhunter());
            orderInfo.setRecommendId(inviteEntry.getPutInUser());
            orderInfo.setMoney(inviteEntry.getHiredBounty());
            orderInfo.setReferrerId(inviteEntry.getPutInUser());
            orderInfo.setIdentification(2);
            orderInfo.setShareUserId(inviteEntry.getSharePerson());
            orderInfo.setCreateTime(LocalDateTime.now());
            orderInfo.setReferrerMoney(stringBigDecimalMap.get(inviteEntry.getPutInUser()));
            orderInfo.setShareMoney(stringBigDecimalMap.get(inviteEntry.getSharePerson()));
            orderInfo.setPlatformMoney(stringBigDecimalMap.get("platformMoney"));
            orderInfo.setNoPaymentTime(inviteEntry.getEntryTime().plusDays(inviteEntry.getWorkday()));
            boolean save = orderInfoService.save(orderInfo);
            if (!save) {
                log.error("创建赏金岗订单投放的岗位为{}投递人为{}",inviteEntry.getPutInPost(),inviteEntry.getPutInUser());
            }
        } else if (inviteEntry.getPostType() == 2) {
            List<OrderInfo> orderInfoList= new ArrayList();
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("post_id",inviteEntry.getPutInPost());
            List<HeadhunterPositionRecord> list = headhunterPositionRecordService.list();
            for (HeadhunterPositionRecord headhunterPositionRecord : list) {
                Map map = DivideIntoUtil.headhuntingFee1(headhunterPositionRecord.getMoney(), inviteEntry.getPutInUser(), inviteEntry.getSharePerson(),headhunterPositionRecord.getMonth());
                OrderInfo orderInfo=new OrderInfo();
                orderInfo.setOrderId(idGenerator.generateCode("OD"));
                orderInfo.setTitle("过保付职位");
                orderInfo.setPostId(inviteEntry.getPutInPost());
                orderInfo.setPutInResumeId(inviteEntry.getPutInResumeId());
                orderInfo.setState(1);
                orderInfo.setJobhunterId(inviteEntry.getPutInJobhunter());
                orderInfo.setRecommendId(inviteEntry.getPutInUser());
                orderInfo.setMoney((BigDecimal) map.get("money"));
                orderInfo.setReferrerId(inviteEntry.getPutInUser());
                orderInfo.setIdentification(3);
                orderInfo.setShareUserId(inviteEntry.getSharePerson());
                orderInfo.setCreateTime(LocalDateTime.now());
                orderInfo.setCreateId(tCompanyPostService.selectCompany(inviteEntry.getPutInPost()));
                orderInfo.setReferrerMoney((BigDecimal) map.get(inviteEntry.getPutInUser()));
                orderInfo.setShareMoney((BigDecimal) map.get(inviteEntry.getSharePerson()));
                orderInfo.setPlatformMoney((BigDecimal) map.get("platformMoney"));
                orderInfo.setNoPaymentTime((LocalDate) map.get("noPaymentTime"));
                orderInfoList.add(orderInfo);
                orderInfo=null;
            }

            boolean b = orderInfoService.saveBatch(orderInfoList);
            if(! b){
                log.error("创建猎头投放失败");

            }
        }
    }

    public void updateRedisPost(String post ,Integer state){

        ValueOperations<String, TPostShare> valueOperations = redisTemplate.opsForValue();
        //1:浏览、 2:下载、3排除  4:约面、5:面试中6淘汰、7:offer、8:入职、9:保证期
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("company_post_id",post);
        TPostShare tPostShare = tPostShareService.getOne(queryWrapper);
        if(state==1){
            tPostShare.setBrowseSize(tPostShare.getBrowseSize()+1);
        }else if(state==2){
            tPostShare.setDownloadSize(tPostShare.getDownloadSize()+1);
        }else if(state==4){
            tPostShare.setSubscribe(tPostShare.getSubscribe()+1);
        }else if(state==5){
            tPostShare.setInterviewSize(tPostShare.getInterviewSize()+1);
        }else if(state==5 ||state==3  ){
            tPostShare.setWeedOut(tPostShare.getWeedOut()+1);
        }else if(state==7){
            tPostShare.setOfferSize(tPostShare.getOfferSize()+1);
        }else if(state==8){
            tPostShare.setEntrySize(tPostShare.getEntrySize()+1);
        }else if(state==9){
            tPostShare.setOverInsured(tPostShare.getOverInsured()+1);
        }
        redisTemplate.delete(Record.POST_RECORD.getValue()+ post);//删除重新插入
        valueOperations.set(Record.POST_RECORD.getValue() + post, tPostShare);
        String s = UUID.randomUUID().toString();
        CorrelationData correlationData1 = new CorrelationData(s);
        Map map=new HashMap();
        map.put("post",post);
        map.put("state",state);
        map.put("uuid",s);
        String mapJson = JSON.toJSONString(map);
        rabbitTemplate.convertAndSend("postRecord.exchange", "postRecord", mapJson , correlationData1);
    }

}
*/
