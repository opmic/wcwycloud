package com.wcwy.websocket.service;

import com.wcwy.websocket.entity.ChatNews;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author Administrator
* @description 针对表【chat_news(消息)】的数据库操作Service
* @createDate 2023-10-24 17:05:23
*/
public interface ChatNewsService extends IService<ChatNews> {

    void addNews(String chatId, Map<String, Object> params);

    /**
     * 查看最新消息
     * @param chatId
     * @return
     */
    List<ChatNews> getList(Set<String> chatId);
}
