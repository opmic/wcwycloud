package com.wcwy.websocket.mapper;

import com.wcwy.websocket.entity.ChatId;
import com.wcwy.websocket.entity.ImFriend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【im_friend】的数据库操作Mapper
* @createDate 2023-12-25 11:40:23
* @Entity com.wcwy.websocket.entity.ImFriend
*/
public interface ImFriendMapper extends BaseMapper<ImFriend> {

    List<ChatId> getUserFriends(@Param("userId") String userId, @Param("state") String state);
}




