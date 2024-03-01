package com.wcwy.websocket.service;

import com.wcwy.websocket.entity.ImMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.websocket.entity.Message;

import java.util.List;

/**
* @author Administrator
* @description 针对表【im_message】的数据库操作Service
* @createDate 2023-12-25 11:40:33
*/
public interface ImMessageService extends IService<ImMessage> {
    /**
     * 读消息，并持久化到redis
     *
     * @param chatId    消息id
     * @param userId    消息读取人
     * @param type      类型
     * @param timestamp 系统时间
     */
    void read(String chatId, String userId, String type, long timestamp) ;
    /**
     * 添加消息到redis 队列
     *
     * @param message 消息
     * @param isRead  是否
     * @return boolean
     * @throws Exception 抛出异常
     */
    boolean save(Message message, boolean isRead) throws Exception;

    /**
     * 读取未读消息，并清空(这里可能是会出现丢数据，未读消息清空后，消息没有发送成功，造成未读列表和已读列表都没有消息)
     * 未读消息只存私聊消息，群聊消息还在群列表里
     * @param chatId 聊天室id
     * @return List
     */
    List<Message> unreadList(String chatId, String fromId);

    /**
     * 查询消息
     *
     * @param chatId   聊天室id
     * @param fromId   userId
     * @param type     聊天类型  私聊 群聊
     * @param pageSize 每页多少条
     * @return List
     */
    List<Message> list(String chatId, String fromId, String type, Long pageSize,Long pageNo);
}
