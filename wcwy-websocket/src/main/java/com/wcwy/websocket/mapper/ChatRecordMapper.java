package com.wcwy.websocket.mapper;

import com.wcwy.websocket.entity.ChatRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【chat_record】的数据库操作Mapper
* @createDate 2023-10-25 14:40:31
* @Entity com.wcwy.websocket.entity.ChatRecord
*/
@Mapper
public interface ChatRecordMapper extends BaseMapper<ChatRecord> {

}




