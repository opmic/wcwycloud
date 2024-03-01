package com.wcwy.websocket.consumer;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcwy.common.utils.MessageMQ;
import com.wcwy.websocket.entity.ChatId;
import com.wcwy.websocket.entity.Message;
import com.wcwy.websocket.service.ChatIdService;
import com.wcwy.websocket.service.ImMessageService;
import com.wcwy.websocket.service.VimUserApiService;
import com.wcwy.websocket.tio.TioWsMsgHandler;
import com.wcwy.websocket.tio.WsOnlineContext;
import com.wcwy.websocket.utils.ChatUtils;
import com.wcwy.websocket.vo.SendInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.utils.hutool.Snowflake;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: MessageConsumer
 * Description:
 * date: 2023/12/28 16:59
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@RocketMQMessageListener(topic = MessageMQ.TOPIC, consumerGroup = MessageMQ.GROUP)
@Component
@Slf4j
public class MessageConsumer implements RocketMQListener<String> {
    @Autowired
    private ThreadPoolExecutor dtpExecutor1;
    @Autowired
    private ImMessageService vimMessageService;
    @Resource
    private RedissonClient redissonClient;
    @Autowired
    private VimUserApiService vimUserApiService;
    @Resource
    private TioWsMsgHandler tioWsMsgHandler;
    @Resource
    private ChatIdService chatIdService;

    @Autowired
    private VimUserApiService vimUserApiService1;

    @Override
    public void onMessage(String message) {

        dtpExecutor1.execute(() -> {
            try {
                System.out.println(message);
                ObjectMapper objectMapper = new ObjectMapper();
                SendInfo sendInfo = objectMapper.readValue(message, SendInfo.class);
                Map<String, Object> map = sendInfo.getMessage();
                map.put("mine", false);
                map.put("timestamp", System.currentTimeMillis());
                map.put("id", String.valueOf(new Snowflake(1L, 1L).nextId()));
                Message message1 = BeanUtil.fillBeanWithMap(map, new Message(), false);

                // 获取锁（可重入），指定锁的名称
                RLock lock1 = redissonClient.getLock("msg:" + message1.getChatId());
                // 尝试获取锁，参数分别是：获取锁的最大等待时间（期间会重试），锁自动释放时间，时间单位
                boolean isLock1 = lock1.tryLock(4, 5, TimeUnit.SECONDS);
                // 判断释放获取成功
                if (isLock1) {
                    try {
                        ChatId userId = chatIdService.getUserId(message1.getChatId());
                        message1.setChatId(String.valueOf(userId.getId()));
                        ChatId userId1 = chatIdService.getUserId(message1.getFromId());
                        message1.setFromId(String.valueOf(userId1.getId()));
                        map.put("chatId", userId.getId());
                        map.put("fromId", userId1.getId());
                        // 获取锁（可重入），指定锁的名称
                        String chatKey = ChatUtils.getChatKey(userId.getId().toString(), userId1.getId().toString(), "0");
                        RLock lock = redissonClient.getLock("addChat" + chatKey);
                        // 尝试获取锁，参数分别是：获取锁的最大等待时间（期间会重试），锁自动释放时间，时间单位
                        boolean isLock = lock.tryLock(4, 5, TimeUnit.SECONDS);
                        // 判断释放获取成功
                        if (isLock) {
                            try {
                                boolean friends = vimUserApiService1.isFriends(userId.getId().toString(), userId1.getId().toString());
                                if (!friends) {
                                    vimUserApiService1.addFriend(userId.getUserId(), userId1.getUserId());
                                }
                            } finally {
                                // 释放锁
                                lock.unlock();
                            }
                        }
                        sendInfo.setMessage(map);
                        ChannelContext channelContextByUser = WsOnlineContext.getChannelContextByUser(message1.getChatId());
                        if (channelContextByUser == null) {
                            vimMessageService.save(message1, false);
                            return;
                        }
                        tioWsMsgHandler.acceptMessage(channelContextByUser, objectMapper, sendInfo);
                    } finally {
                        // 释放锁
                        lock1.unlock();
                    }
                }


            } catch (Exception e) {
                log.error("消息通知报错" + e);
            }
        });

    }
}