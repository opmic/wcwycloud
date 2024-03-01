package com.wcwy.websocket.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.websocket.entity.ChatNews;
import com.wcwy.websocket.entity.ChatRecord;
import com.wcwy.websocket.service.ChatNewsService;
import com.wcwy.websocket.mapper.ChatNewsMapper;
import com.wcwy.websocket.service.ChatRecordService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author Administrator
* @description 针对表【chat_news(消息)】的数据库操作Service实现
* @createDate 2023-10-24 17:05:23
*/
@Service
public class ChatNewsServiceImpl extends ServiceImpl<ChatNewsMapper, ChatNews>
    implements ChatNewsService{
    @Autowired
    private ChatRecordService chatRecordService;



    @Autowired
    private ChatNewsMapper chatNewsMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNews(String chatId, Map<String, Object> params) {
        String toUserId = params.get("toUserId").toString();
        String fromUserId = params.get("fromUserId").toString();
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("chat_id",chatId);
        queryWrapper.eq("user_id",toUserId);
        ChatRecord one = chatRecordService.getOne(queryWrapper);
        one.setUnread(one.getUnread()+1);
        one.setUpdateTime(LocalDateTime.now());
        boolean b = chatRecordService.updateById(one);
        if(b){
            ChatNews chatNews=new ChatNews();
            chatNews.setChatId(chatId);
            chatNews.setToId(toUserId);
            chatNews.setFromId(fromUserId);
            chatNews.setSendTime(LocalDateTime.now());
            chatNews.setType(0);
            chatNews.setReadStatus(0);
            chatNews.setMsg(JSON.toJSONString(params));
            this.save(chatNews);
          //  redisUtils.leftPush(Cache.UNREAD_CACHE_NEWS.getKey()+add.getId(), );
        }
    }

    @Override
    public List<ChatNews> getList(Set<String> chatId) {
        return chatNewsMapper.getList(chatId);
    }


}




