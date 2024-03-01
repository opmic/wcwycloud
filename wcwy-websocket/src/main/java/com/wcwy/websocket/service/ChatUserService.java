package com.wcwy.websocket.service;

import com.wcwy.websocket.dto.ChatUserDTO;
import com.wcwy.websocket.entity.ChatUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【chat_user(聊天主表)】的数据库操作Service
* @createDate 2023-10-24 17:05:13
*/
public interface ChatUserService extends IService<ChatUser> {
    ChatUser add(Map<String, Object> params);


    /**
     * 获取聊天人员
     * @param userid
     * @return
     */
    List<ChatUserDTO> getList(String userid);


}
