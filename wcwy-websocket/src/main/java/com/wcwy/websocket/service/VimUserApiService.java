package com.wcwy.websocket.service;


import com.wcwy.websocket.vo.User;

import java.util.List;

public interface VimUserApiService {

    List<User> getFriends(String userId);
    User get(String userId);


    boolean addFriends(String friendId,String userId);

    User SystemUser(String userId);
    boolean addFriend(String friendId, String userId);
    /**
     * 判断是否是好友
     * @param friendId
     * @param userId
     * @return
     */
    boolean isFriends(String friendId, String userId);

    void loginTime(String id);
/*    List<User> getByDept(String deptId);

    List<User> search(String mobile);



    int update(User user);


    boolean delFriends(String friendId,String userId);



    int save(User user);*/
}
