package com.wcwy.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.websocket.dto.ChatUserDTO;
import com.wcwy.websocket.entity.ChatRecord;
import com.wcwy.websocket.entity.ChatUser;
import com.wcwy.websocket.service.ChatNewsService;
import com.wcwy.websocket.service.ChatRecordService;
import com.wcwy.websocket.service.ChatUserService;
import com.wcwy.websocket.mapper.ChatUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【chat_user(聊天主表)】的数据库操作Service实现
* @createDate 2023-10-24 17:05:13
*/
@Service
public class ChatUserServiceImpl extends ServiceImpl<ChatUserMapper, ChatUser>
    implements ChatUserService{
    @Autowired
    private IDGenerator idGenerator;

    @Autowired
    private ChatNewsService chatNewsService;
    @Autowired
    private ChatUserMapper chatUserMapper;
    @Autowired
    private ChatRecordService chatRecordService;

    @Override
    public ChatUser add(Map<String, Object> params) {
        String toUserId = params.get("toUserId").toString();
        String fromUserId = params.get("fromUserId").toString();
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",fromUserId);
        queryWrapper.eq("another_id",toUserId);
        ChatUser one = this.getOne(queryWrapper);
        if(one==null){
            QueryWrapper queryWrapper1=new QueryWrapper();
            queryWrapper1.eq("user_id",toUserId);
            queryWrapper1.eq("another_id",fromUserId);
             one= this.getOne(queryWrapper1);
            if(one==null){

                one=new ChatUser();
                one.setId(idGenerator.generateCode("CHAT"));
                one.setUserId(fromUserId);
                one.setAnotherId(toUserId);
                boolean save = this.save(one);
                if(save){
                    List<ChatRecord> list=new ArrayList<>(2);
                    ChatRecord chatRecord1=new ChatRecord();
                    chatRecord1.setChatId(one.getId());
                    chatRecord1.setUnread(0);
                    chatRecord1.setUserId(fromUserId);
                    list.add(chatRecord1);
                    if( ! toUserId.equals(fromUserId)){
                        ChatRecord chatRecord2=new ChatRecord();
                        chatRecord2.setChatId(one.getId());
                        chatRecord2.setUnread(0);
                        chatRecord2.setUserId(toUserId);
                        list.add(chatRecord2);
                    }
                    boolean b = chatRecordService.saveBatch(list);
                }
            }

        }


        return one;
    }

    @Override
    public List<ChatUserDTO> getList(String userid) {
        return chatUserMapper.getList(userid);
    }



}




