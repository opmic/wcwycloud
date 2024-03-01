package com.wcwy.websocket.mapper;

import com.wcwy.websocket.entity.ChatNews;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
* @author Administrator
* @description 针对表【chat_news(消息)】的数据库操作Mapper
* @createDate 2023-10-24 17:05:23
* @Entity com.wcwy.websocket.entity.ChatNews
*/
@Mapper
public interface ChatNewsMapper extends BaseMapper<ChatNews> {

    List<ChatNews> getList(@Param("chatId") Set<String> chatId);
}




