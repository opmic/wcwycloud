package com.wcwy.websocket.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcwy.websocket.entity.ImMessage;
import com.wcwy.websocket.entity.Message;
import com.wcwy.websocket.service.ImMessageService;
import com.wcwy.websocket.mapper.ImMessageMapper;
import com.wcwy.websocket.utils.ChatUtils;
import com.wcwy.websocket.vo.SendInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【im_message】的数据库操作Service实现
* @createDate 2023-12-25 11:40:33
*/
@Service
public class ImMessageServiceImpl extends ServiceImpl<ImMessageMapper, ImMessage>
    implements ImMessageService{
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ImMessageService iImMessageService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 读消息，并持久化到redis
     *
     * @param chatId    消息id
     * @param userId    消息读取人
     * @param type      类型
     * @param timestamp 系统时间
     */
    @Override
    public void read(String chatId, String userId, String type, long timestamp) {
        String key = ChatUtils.getReadKey(userId, chatId);
        redisTemplate.opsForValue().set(key, String.valueOf(timestamp));
    }

    /**
     * 添加消息到redis 队列，有可能受到的是离线消息，所以要去删除下离线消息里面的记录
     *
     * @param message 消息
     * @param isRead  是否
     * @return boolean
     * @throws JsonProcessingException 抛出异常
     */
    @Override
    public boolean save(Message message, boolean isRead) throws Exception {
        //先保存消息
        String key = isRead ? ChatUtils.getChatKey(message.getFromId(), message.getChatId(), message.getType()) : StrUtil.format(ChatUtils.UNREAD_TEMPLATE, message.getChatId());
        boolean res = Boolean.TRUE.equals(redisTemplate.opsForZSet().add(key, objectMapper.writeValueAsString(message), message.getTimestamp()));
        //如果是离线消息，去离线队列里处理
        if (message.isOffline()) {
            String unreadKey = StrUtil.format(ChatUtils.UNREAD_TEMPLATE, message.getChatId());
            redisTemplate.opsForZSet().removeRangeByScore(unreadKey, message.getTimestamp(), message.getTimestamp());
        }
        if (isRead) {
            //异步保存到数据库
            new SaveChatMessageThread(message).run();
        }
        return res;
    }
    /**
     * 内部类 保存到数据库
     */
    class SaveChatMessageThread implements Runnable {

        private final Message message;

        public SaveChatMessageThread(Message message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {
                ImMessage imMessage = new ImMessage();
                imMessage.setId(message.getId());
                imMessage.setExtend(objectMapper.writeValueAsString(message));
                imMessage.setMessageType(message.getMessageType());
                imMessage.setContent(message.getContent());
                imMessage.setFromId(message.getFromId());
                imMessage.setToId(message.getChatId());
                imMessage.setSendTime(message.getTimestamp());
                //聊天的唯一
                imMessage.setChatKey(ChatUtils.getChatKey(message.getFromId(), message.getChatId(), message.getType()));
                iImMessageService.saveOrUpdate(imMessage);
            } catch (JsonProcessingException e) {
                log.error("保存消息进入数据库失败，JSON格式化问题");
            }
        }
    }

    /**
     * 读取未读消息
     * 未读消息只存私聊消息，群聊消息还在群列表里
     *
     * @param chatId 聊天室id = toUserId
     * @param fromId 发送人id
     * @return List
     */
    @Override
    public List<Message> unreadList(String chatId, String fromId) {
        String key = StrUtil.format(ChatUtils.UNREAD_TEMPLATE, chatId);
        Set<String> set = redisTemplate.opsForZSet().range(key, 0, -1);
        if (set != null) {
            return set.stream().filter(str -> {
                try {
                    Message message = new ObjectMapper().readValue(str, Message.class);
                    //如果发送人为空，取出所有的未读消息
                    return StrUtil.isBlank(fromId) || message.getFromId().equals(fromId);
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                    return false;
                }

            }).map(this::toMessage).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 查询消息
     *
     * @param chatId   聊天室id
     * @param fromId   发送人
     * @param chatType     聊天类型  私聊 群聊
     * @param pageSize 每页多少条
     * @return List
     */
    @Override
    public List<Message> list(String chatId, String fromId, String chatType, Long pageSize,Long pageNo) {
        String key = ChatUtils.getChatKey(fromId, chatId, chatType);
        Long a=0L;
        Long b=pageSize-1;
        if(pageNo>1){
            a= pageNo*pageSize;
        }
        Long c=a+b;
        Long size = redisTemplate.opsForZSet().size(key);
        if(size<a){
            return null;
        }
       /* Long d=  size/pageSize;
        if(pageNo > d ){
            return null;
        }*/
        System.out.println(a);
        System.out.println(c);
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(key,a , c);
        if(typedTuples !=null && typedTuples.size() !=0){
            List<Message> list = typedTuples.stream().map(this::toMessage).collect(Collectors.toList());
            Collections.reverse(list);
            //加上未读消息，分页第一页的数据是不一定和pageSize一样多，
            list.addAll(unreadList(chatId, fromId));
            return list;
        }else {
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("chat_key",key);
            List<ImMessage> list = this.list(queryWrapper);
            for (ImMessage imMessage : list) {
                if (imMessage.getExtend() != null) {
                    try {
                        Message message1 = objectMapper.readValue(imMessage.getExtend(), Message.class);
                        redisTemplate.opsForZSet().add(key, objectMapper.writeValueAsString(message1), message1.getTimestamp());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
              /*  message.setMessageType(imMessage.getMessageType());
                message.setContent(imMessage.getContent());
                message.setFromId(imMessage.getFromId());
                message.setChatId(imMessage.getToId());
                message.setTimestamp(imMessage.getSendTime());
                try {
                    redisTemplate.opsForZSet().add(key, objectMapper.writeValueAsString(message), message.getTimestamp());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }*/
            }
            Set<ZSetOperations.TypedTuple<String>> typedTuples1 = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, pageSize - 1);
            if(typedTuples1 !=null) {
                List<Message> list1 = typedTuples1.stream().map(this::toMessage).collect(Collectors.toList());
                Collections.reverse(list);
                //加上未读消息，分页第一页的数据是不一定和pageSize一样多，
                list1.addAll(unreadList(chatId, fromId));
                return list1;
            }

        }
       /* Set<String> set = redisTemplate.opsForZSet().reverseRange(key, 0, pageSize - 1);
        if (set != null) {
            List<Message> list = set.stream().map(this::toMessage).collect(Collectors.toList());
            Collections.reverse(list);
            //加上未读消息，分页第一页的数据是不一定和pageSize一样多，
            list.addAll(unreadList(chatId, fromId));
            return list;
        }*/
        return new ArrayList<>();
    }


    /**
     * json 转 message
     *
     * @param str str
     * @return Message;
     */
    private Message toMessage(String str) {
        try {
            Message message = new ObjectMapper().readValue(str, Message.class);
            message.setOffline(true);
            return message;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
    private Message toMessage( ZSetOperations.TypedTuple<String> typedTuple) {
        try {
            Message message = new ObjectMapper().readValue(typedTuple.getValue(), Message.class);
            message.setOffline(true);
            return message;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}




