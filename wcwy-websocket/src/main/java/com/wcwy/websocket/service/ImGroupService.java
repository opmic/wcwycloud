package com.wcwy.websocket.service;

import com.wcwy.websocket.entity.ImGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.websocket.vo.Group;
import com.wcwy.websocket.vo.User;

import java.util.List;

/**
* @author Administrator
* @description 针对表【im_group】的数据库操作Service
* @createDate 2023-12-25 11:40:28
*/
public interface ImGroupService extends IService<ImGroup> {
    List<Group> getGroups(String userId);

/*    List<User> getUsers(String groupId);

    Group get(String groupId);

    boolean addUsers(String groupId,String[] userIds);

    boolean delUsers(String groupId,String[] userIds);

    int del(String groupId);*/


/*    Group save(Group group);

    int update(Group group);*/
}
