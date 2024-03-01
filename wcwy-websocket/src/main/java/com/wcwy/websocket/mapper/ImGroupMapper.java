package com.wcwy.websocket.mapper;

import com.wcwy.websocket.entity.ImGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.ScanIterator;

/**
* @author Administrator
* @description 针对表【im_group】的数据库操作Mapper
* @createDate 2023-12-25 11:40:28
* @Entity com.wcwy.websocket.entity.ImGroup
*/
public interface ImGroupMapper extends BaseMapper<ImGroup> {
    /**
     * 查询用户的群
     *
     * @param userId 用户id
     * @return 群集合
     */
    ScanIterator<ImGroup> getUserGroups(String userId);
}




