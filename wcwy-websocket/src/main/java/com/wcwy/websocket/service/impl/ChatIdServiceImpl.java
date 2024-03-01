package com.wcwy.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.websocket.entity.ChatId;
import com.wcwy.websocket.service.ChatIdService;
import com.wcwy.websocket.mapper.ChatIdMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【chat_id(对应用户聊天id)】的数据库操作Service实现
* @createDate 2023-12-25 15:07:14
*/
@Service
public class ChatIdServiceImpl extends ServiceImpl<ChatIdMapper, ChatId>
    implements ChatIdService{
    private static final String CACHE_KEY = "chat";
    @Override
    @Cacheable(value = CACHE_KEY + ":friend", key = "#userId")
    public ChatId getUserId(String userId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        ChatId one = this.getOne(queryWrapper);
        if(one==null){
            ChatId chatId=new ChatId();
            chatId.setUserId(userId);
            this.save(chatId);
            one = this.getOne(queryWrapper);
        }
        return one;
    }
}




