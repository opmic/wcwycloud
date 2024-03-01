package com.wcwy.websocket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.websocket.entity.ChatRecord;
import com.wcwy.websocket.service.ChatRecordService;
import com.wcwy.websocket.mapper.ChatRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【chat_record】的数据库操作Service实现
* @createDate 2023-10-25 14:40:31
*/
@Service
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord>
    implements ChatRecordService{

}




