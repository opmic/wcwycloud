package com.wcwy.post.consumer;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.redis.enums.Record;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.PostOrder;
import com.wcwy.post.entity.OrderInfo;
import com.wcwy.post.entity.TCompanyPost;
import com.wcwy.post.entity.TPayConfig;
import com.wcwy.post.entity.TPostShare;
import com.wcwy.post.po.ParticularsPO;
import com.wcwy.post.produce.PostRecordProduce;
import com.wcwy.post.service.HeadhunterPositionRecordService;
import com.wcwy.post.service.OrderInfoService;
import com.wcwy.post.service.TCompanyPostService;
import com.wcwy.post.service.TPostShareService;
import com.wcwy.system.annotation.RocketLog;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * ClassName: PostOrderProduce
 * Description:
 * date: 2022/12/9 15:02
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RocketMQMessageListener(topic = PostOrder.TOPIC, consumerGroup = PostOrder.GROUP)
@Component
@Slf4j
public class PostOrderConsumer implements RocketMQListener<String> {
    @Autowired
    private OrderInfoService orderInfoService;
    @Resource
    private IDGenerator idGenerator;
    @Autowired
    private TPostShareService tPostShareService;
    @Autowired
    private TCompanyPostService tCompanyPostService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private HeadhunterPositionRecordService headhunterPositionRecordService;

    @Autowired
    private PostRecordProduce postRecordProduce;
    DecimalFormat df = new DecimalFormat("#.0000");

    @SneakyThrows
    @Override
    @RocketLog(title = "生成订单", businessType = 1)
    public void onMessage(String msg) {

        TPayConfig tPayConfig = (TPayConfig) redisUtils.get(RedisCache.TPAYCONFIG.getValue());
        Map inviteEntry = JSON.parseObject(msg, Map.class);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean aBoolean = valueOperations.setIfAbsent(Sole.INSERT_ORDER.getKey() + inviteEntry.get("put_in_resume_id"), inviteEntry.get("put_in_resume_id"));
        if (!aBoolean) {
            return;
        }
        try {
            //  this.updateRedisPost(inviteEntry.getPutInPost(), 8);
            //根据类型转化
            BigDecimal money = null;
            if (inviteEntry.get("hired_bounty") instanceof Integer) {
                money = new BigDecimal((Integer) inviteEntry.get("hired_bounty"));
            } else if (inviteEntry.get("hired_bounty") instanceof BigDecimal) {
                money = (BigDecimal) inviteEntry.get("hired_bounty");
            } else if (inviteEntry.get("hired_bounty") instanceof Double) {
                money = new BigDecimal((Double) inviteEntry.get("hired_bounty"));
            }
            money = money.multiply(new BigDecimal(10000)).divide(new BigDecimal(12), 3, BigDecimal.ROUND_HALF_UP);
            if ((Integer) inviteEntry.get("post_type") != 0) {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderId(idGenerator.generateCode("OD"));

                if ((Integer) inviteEntry.get("post_type") == 1) {
                    orderInfo.setTitle("入职付职位");
                    orderInfo.setIdentification(2);
                    orderInfo.setNoPaymentTime(LocalDate.parse(inviteEntry.get("entryDate").toString()));//支付时间);
                    money = money.multiply(new BigDecimal(tPayConfig.getEntryPayment()).multiply(new BigDecimal(0.01)));
                    String dstr4 = df.format(money);
                    String str4 = dstr4.substring(0, dstr4.indexOf('.') + 3);
                    orderInfo.setMoney(new BigDecimal(str4));
                } else if ((Integer) inviteEntry.get("post_type") == 2) {
                    orderInfo.setTitle("满月付职位");
                    orderInfo.setIdentification(3);
                    orderInfo.setNoPaymentTime(LocalDate.parse(inviteEntry.get("entryDate").toString()).plusMonths(1));//支付时间);
                    money = money.multiply(new BigDecimal(tPayConfig.getFullMoonPayment()).multiply(new BigDecimal(0.01)));
                    String dstr4 = df.format(money);
                    String str4 = dstr4.substring(0, dstr4.indexOf('.') + 3);
                    orderInfo.setMoney(new BigDecimal(str4));
                } else if ((Integer) inviteEntry.get("post_type") == 3) {
                    orderInfo.setTitle("到面付职位");
                    orderInfo.setIdentification(5);
                    orderInfo.setNoPaymentTime(LocalDate.now());//支付时间);
                    money = money.multiply(new BigDecimal(tPayConfig.getInterviewPayment()).multiply(new BigDecimal(0.01)));
                    String dstr4 = df.format(money);
                    String str4 = dstr4.substring(0, dstr4.indexOf('.') + 3);
                    orderInfo.setMoney(new BigDecimal(str4));
                } else {
                    orderInfo.setTitle("入职付职位");
                    orderInfo.setIdentification(2);

                }
                orderInfo.setPostId(inviteEntry.get("post_id").toString());
                orderInfo.setPutInResumeId(inviteEntry.get("put_in_resume_id").toString());
                orderInfo.setState(1);
                orderInfo.setCreateId(inviteEntry.get("create_id").toString());
                orderInfo.setCreateName(inviteEntry.get("create_name").toString());
                orderInfo.setPayer(inviteEntry.get("payer").toString());
                orderInfo.setJobhunterId(inviteEntry.get("putInJobhunter").toString());
                orderInfo.setRecommendId(inviteEntry.get("referrer_id").toString());
                orderInfo.setCreateTime(LocalDateTime.now());
                if (!StringUtils.isEmpty(inviteEntry.get("recommend_time"))) {
                    String recommend_time = inviteEntry.get("recommend_time").toString();
                    LocalDateTime localDate=LocalDateTime.parse(recommend_time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    orderInfo.setRecommendTime(localDate);
                }

                ParticularsPO particularsPO = new ParticularsPO();
                particularsPO.setAvatar(inviteEntry.get("avatar").toString());
                particularsPO.setJobHunter(inviteEntry.get("jobHunter").toString());
                particularsPO.setUserName(inviteEntry.get("userName").toString());
                if(inviteEntry.get("workTime") !=null){
                    particularsPO.setWorkTime(LocalDate.parse(inviteEntry.get("workTime").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
                particularsPO.setEducation(inviteEntry.get("education").toString());
                particularsPO.setBirthday(LocalDate.parse(inviteEntry.get("birthday").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                if (!StringUtils.isEmpty(inviteEntry.get("post_id").toString())) {
                    TCompanyPost byId = tCompanyPostService.getById(inviteEntry.get("post_id").toString());
                    if ((Integer) inviteEntry.get("post_type") == 1 || (Integer) inviteEntry.get("post_type") == 2) {
                        particularsPO.setEntryTime(LocalDateTime.parse(inviteEntry.get("entryTime").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    } else if ((Integer) inviteEntry.get("post_type") == 3) {
                        particularsPO.setInterviewTime(LocalDateTime.parse(inviteEntry.get("dateTime").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    }
                    particularsPO.setPostType(byId.getPostType());
                    particularsPO.setPostLabel(byId.getPostLabel());
                    particularsPO.setBeginSalary(byId.getBeginSalary());
                    particularsPO.setEndSalary(byId.getEndSalary());
                    particularsPO.setCompanyName(byId.getCompanyName());
                }

                orderInfo.setParticulars(JSON.toJSONString(particularsPO));
                boolean save = orderInfoService.save(orderInfo);
                if (!save) {
                    log.error("创建猎头投放失败");
                    redisTemplate.delete(Sole.INSERT_ORDER.getKey() + inviteEntry.get("put_in_resume_id"));
                    throw new Exception();
                }
            }

        } catch (Exception e) {
            redisTemplate.delete(Sole.INSERT_ORDER.getKey() + inviteEntry.get("put_in_resume_id"));
            redisUtils.acceptSet(PostOrder.GROUP, msg);
            throw new Exception(e);
        }


    }


    public void updateRedisPost(String post, Integer state) {

        ValueOperations<String, TPostShare> valueOperations = redisTemplate.opsForValue();
        //1:浏览、 2:下载、3排除  4:约面、5:面试中6淘汰、7:offer、8:入职、9:保证期
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("company_post_id", post);
        TPostShare tPostShare = tPostShareService.getOne(queryWrapper);
        if (state == 1) {
            tPostShare.setBrowseSize(tPostShare.getBrowseSize() + 1);
        } else if (state == 2) {
            tPostShare.setDownloadSize(tPostShare.getDownloadSize() + 1);
        } else if (state == 4) {
            tPostShare.setSubscribe(tPostShare.getSubscribe() + 1);
        } else if (state == 5) {
            tPostShare.setInterviewSize(tPostShare.getInterviewSize() + 1);
        } else if (state == 5 || state == 3) {
            tPostShare.setWeedOut(tPostShare.getWeedOut() + 1);
        } else if (state == 7) {
            tPostShare.setOfferSize(tPostShare.getOfferSize() + 1);
        } else if (state == 8) {
            tPostShare.setEntrySize(tPostShare.getEntrySize() + 1);
        } else if (state == 9) {
            tPostShare.setOverInsured(tPostShare.getOverInsured() + 1);
        }
        redisTemplate.delete(Record.POST_RECORD.getValue() + post);//删除重新插入
        valueOperations.set(Record.POST_RECORD.getValue() + post, tPostShare);
        String s = UUID.randomUUID().toString();
        Map map = new HashMap();
        map.put("post", post);
        map.put("state", state);
        map.put("uuid", s);
     /*   String mapJson = JSON.toJSONString(map);
        rabbitTemplate.convertAndSend("postRecord.exchange", "postRecord", mapJson, correlationData1);*/
        postRecordProduce.sendSyncMessage(map);
    }
}
