package com.wcwy.websocket.service;

import com.wcwy.websocket.entity.ChatId;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【chat_id(对应用户聊天id)】的数据库操作Service
* @createDate 2023-12-25 15:07:14
*/
public interface ChatIdService extends IService<ChatId> {

    /**
     * 获取聊天id
     * @param userId
     * @return
     */

    ChatId getUserId(String userId);
}
