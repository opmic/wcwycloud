package com.wcwy.websocket.mapper;

import com.wcwy.websocket.dto.ChatUserDTO;
import com.wcwy.websocket.entity.ChatUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【chat_user(聊天主表)】的数据库操作Mapper
* @createDate 2023-10-24 17:05:13
* @Entity com.wcwy.websocket.entity.ChatUser
*/
@Mapper
public interface ChatUserMapper extends BaseMapper<ChatUser> {


    /****
     * 获取聊天室
     * @param userid
     * @return
     */
    List<ChatUserDTO> getList(@Param("userid") String userid);


}




