package com.wcwy.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wawy.company.api.TCompanyApi;
import com.wcwy.common.base.utils.LocalDateTimeUtils;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.websocket.entity.ChatId;
import com.wcwy.websocket.entity.ImFriend;
import com.wcwy.websocket.exception.VimBaseException;
import com.wcwy.websocket.mapper.ImFriendMapper;
import com.wcwy.websocket.service.ChatIdService;
import com.wcwy.websocket.service.ImFriendService;
import com.wcwy.websocket.service.VimUserApiService;
import com.wcwy.websocket.session.CompanyMetadata;
import com.wcwy.websocket.utils.SysUtils;
import com.wcwy.websocket.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * vim 用户操作类，如果需要对接其他的系统，重新下面的方法即可
 *
 * @author 乐天
 */
@Service
public class VimUserApiServiceImpl implements VimUserApiService {

    private static final String CACHE_KEY = "user";

/*    @Resource
    private ISysUserService iSysUserService;*/
    @Autowired
    private  RedisUtils redisUtils;
    @Autowired
    private ThreadPoolExecutor dtpExecutor1;
    @Resource
    private ImFriendMapper imFriendMapper;
    @Resource
    private ChatIdService chatIdService;
    @Resource
    private ImFriendService iImFriendService;
    @Resource
    private TCompanyApi tCompanyApi;
    @Resource
    private CompanyMetadata companyMetadata;
    /**
     * 获取用户的好友 同时缓存
     *
     * @param userId 用户id
     * @return List<User>
     */
    @Override
    @Cacheable(value = CACHE_KEY + ":friend", key = "#userId")
    public List<User> getFriends(String userId) {
        List<User> list=new ArrayList<>();
        ChatId chatId= chatIdService.getUserId(userId);
       List<ChatId> stringList= imFriendMapper.getUserFriends(String.valueOf(chatId.getId()), "0");
        List<String> customerIds = stringList.stream().map(ChatId::getUserId).collect(Collectors.toList());
        Map<String, Map> stringMapMap = tCompanyApi.selectUser(customerIds);
        for (ChatId s : stringList) {
            //系统机器人
            //SY001 无忧福利官 SY002 无忧小秘书(通知) SY003 无忧小秘书(举报)
            boolean equals = "SY".equals(s.getUserId().substring(0, 2));
            if(equals){
                User user = SystemUser(s.getUserId());
                list.add(user);
                continue;
            }
            User user=new User();
            Map map = stringMapMap.get(s.getUserId());
            if(map==null){
                continue;
            }
            try {
                user.setId(s.getId().toString());
                user.setName(map.get("name").toString());
                user.setUserId(map.get("id").toString());
                user.setAvatar(map.get("avatar").toString());
                user.setMobile(map.get("mobile").toString());
                user.setSex(map.get("sex").toString());
                user.setType(0);
            }catch (Exception e){
                continue;
            }
            list.add(user);
        }
        return list;
    }

    /*无忧机器人分类*/
    @Override
    public User SystemUser(String userId){
        //SY001 无忧福利官 SY002 无忧小秘书(通知) SY003 无忧小秘书(举报)
        User user=new User();
        ChatId chatId= chatIdService.getUserId(userId);
        user.setId(chatId.getId().toString());
        if("SY001".equals(userId)){
            user.setName("无忧福利官");
            user.setAvatar("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAC4AAAAuCAYAAABXuSs3AAAAAXNSR0IArs4c6QAABQxJREFUaEPdmstTHFUUh3/ndvNMJARMUGCSDHlUCI9K1GgBJlWCFS01caNZuABLF65YWFb8I5TShW6s8lFk4SKuJGoZk5AyGihfRE1mBkzCZDIMCpgHmPAYuu+xbg9DMWGY7p4JOHiX0+ec+/Xpc++559whZDjGw76SNZp4giEbiakWzFUQYgOY11qmiW5DyjEQDTLxRYLouWPKM+s8NTcymZrSUeZgMN/MmTrM4DYStB8M3ZUdggHms2Dq1GYLjpHXO+1KX/nDjQKHewoMUdxO4DcA2uhGd2lZHmVQhy5vvU+eximnNh2DG5GLB4nEe8zY7NS4GzkihJhlu15Re9yJni14LCwm3wXRa26/kBOAu2QYzB9os4Wv24VPSnAe6y83ovI4AQ+lAZG2CgN9szJ6qNCzO7KUkSXBeeSPKtMwuoHlCQ0HbxXSdL2ZynYMJpNNCs7X+stNTfb8h9Bx1pBmikbatHP4bvhF4BwOFxhi4hyB9jjwyrKLMPi8LouayONJ2HEWgRsR/4cAXl12IlcT8Md6RU0CUwK42vIA8bnb3YMGzkMM+iC314O31SdFoku/QVz+HXJrLXiH64/JgHx+4VY5D66SiymKA+nEtfj+C9BELIPL6r1gb3UCPAV+hgj6rd+4qATy8edc+XtOOKTJW9XxJDUPPhvxHyHgrXQs4vpf0H46BUgZg69vAlduBZhhQV9V/gBY0yH3tgAlZWlNQ8CbWsWut5WyBc7BM/lmbtlVAOlZVIZGrkH0fWvBgghyz36oFxKhgRi0ngP5iILO6KQwokULtqjkZIEbQ/5WEDpt3RCdATjm1aR769AViIG+xY+EBrl7H3j9hqWnEALIybNFgECb/uCuozHwSOA0wM2ptOhCL0T4kr3hDCSkZzu4rsHGAnXrFdUtxGFfiSloFICWSkP76mgGSM5VzWda7YTNSaYyMoZ8L4DoMzvpLAIHhHiRzOFAB7M6X6ce2QTOzO+QEfGfAHBgNYEDOKnA1YrbZgcuTnwKMg07sYyeW/v8Uy85sXFZgY8DKLKTpuEgSCWS6Axo8h87cVfPufA+IDcPvKUaXO611yUaJ2PYP+um2CWVVH74xt64Cwn52AFw6QPONQjGKgZ3GCpxdzj1OJdXqUQPFWJ2w7XHgQnHi9MNOK8rhWx61lIR574EjV9PyZ4GuLU4HW2HrsDvL4d89MkY+I+nQH8vqrwSXiQN8JOOE1A2gccSkMOUn03gVsqfGAqUFhKP2B2ysgjc1NQhSwE5Oda6Al9bDLnvYCzGv+sC3VY5bunhLsbnjrUW+J/+VkgHhYSqdBwmoHjRQDfH7HZDuAGXLF/OraztnCvdgvlm7pSz0u3mKLTer21h3AiYDU8D6x2VdImlm5rEjPiPsJNiOToD7fSxWG15LwYRzJbD1lnFbiwqlpWCm/YEBX6BCPrs5nH0XHprwNUPO5FN3p6ILVKrIdRla0W1HQZ9oFA/aHrSVjyZAOcXgjfvBFfVWF0B+yEPJW0IxRWNiP8jAK/YG1pBCeZP9MqaBKb/T9PTivfV2GaOB0A2NPYN02zJ31R3JVlQplwVk+FfK/JEbhev8FUKAX0z6V6lzHt+NV5eLfxEq+66cCG8SlJSFLfzPb6gJWgdQt5YngvahBeYuxIHURuI07wSp7MQ3KlNr8CVeNIMqJqmutbMbDaAqU4AXgY2ArQmJs93CBhlIMjEF4i0Xs0wuynDPyH8C/GLhDe4jGrOAAAAAElFTkSuQmCC");
            user.setType(1);
        }else if("SY002".equals(userId)){
            user.setName("无忧小秘书");
            user.setAvatar("data:image/png;base64,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");
            user.setType(1);
        }else if("SY003".equals(userId)){
            user.setName("无忧小秘书");
            user.setAvatar("data:image/png;base64,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");
            user.setType(2);
        }else if("SY004".equals(userId)){
            user.setName("无忧小助手");
            user.setAvatar("data:image/png;base64,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");
            user.setType(0);
        }

        return user;
    }

    @Override
    public boolean isFriends(String friendId, String userId) {
        QueryWrapper<ImFriend> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("friend_id", friendId);
        boolean f0 = iImFriendService.count(wrapper)==1;

        QueryWrapper<ImFriend> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id", friendId);
        wrapper1.eq("friend_id", userId);
        boolean f1 = iImFriendService.count(wrapper1)==1;

        return f0 || f1;
    }

    @Override
    public void loginTime(String id) {
        dtpExecutor1.execute(() -> {
            ChatId byId = chatIdService.getById(id);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.now();
            String dateStr = localDateTime.format(fmt);
            Object o = redisUtils.get("loginTime:" + byId.getUserId());
            if(StringUtils.isEmpty(o)){
                redisUtils.set("loginTime:"+byId.getUserId(),dateStr,60*5);

            }else {
                redisUtils.set("loginTime:"+byId.getUserId(),o,60*5);
            }

           // redisUtils.incr("loginTimeSum:"+byId.getUserId(),LocalDateTimeUtils.convert());
        });

    }

    /**
 * 根据用户id获取用户,同时缓存用户
 *
 * @param userId 用户ID
 * @return User
 */
    @Override
    @Cacheable(value = CACHE_KEY + ":one", key = "#userId")
    public User get(String userId) {
        ChatId chatId= chatIdService.getById(userId);
        boolean equals = "SY".equals(chatId.getUserId().substring(0, 2));
        if(equals){
            User user = SystemUser(chatId.getUserId());
            return user;
        }

        ArrayList<String> strings = new ArrayList<>();
        strings.add(chatId.getUserId());
        Map<String, Map> stringMapMap = tCompanyApi.selectUser(strings);
        Map map = stringMapMap.get(chatId.getUserId());
        return new User(String.valueOf(userId), String.valueOf(map.get("name")), String.valueOf(map.get("avatar")), String.valueOf(map.get("mobile")), String.valueOf(map.get("sex")), String.valueOf( map.get("id")));
    }
    /**
     * 添加好友同时清除双方的好友缓存关系
     *
     * @param friendId 好友id
     * @param userId   用户id
     * @return boolean
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_KEY + ":friend", key = "#userId"),
            @CacheEvict(value = CACHE_KEY + ":friend", key = "#friendId")
    })
    public boolean addFriends(String friendId, String userId) {
        ChatId chatId= chatIdService.getUserId(userId);
        ChatId chatId1= chatIdService.getUserId(friendId);

        List<ChatId> stringList= imFriendMapper.getUserFriends(String.valueOf(chatId.getId()), "0");
        List<String> customerIds = stringList.stream().map(ChatId::getUserId).collect(Collectors.toList());
        if (customerIds.contains(friendId)) {
            throw new VimBaseException("该好友已存在!", null);
        }
        ImFriend imFriend = new ImFriend();
        imFriend.setFriendId(chatId1.getId());
        imFriend.setUserId(chatId.getId());
        imFriend.setState(SysUtils.FRIEND_STATUS_COMMON);
        iImFriendService.save(imFriend);
        return true;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_KEY + ":friend", key = "#userId"),
            @CacheEvict(value = CACHE_KEY + ":friend", key = "#friendId")
    })
    public boolean addFriend(String friendId, String userId) {
        ChatId chatId= chatIdService.getUserId(userId);
        ChatId chatId1= chatIdService.getUserId(friendId);

        List<ChatId> stringList= imFriendMapper.getUserFriends(String.valueOf(chatId.getId()), "0");
        List<String> customerIds = stringList.stream().map(ChatId::getUserId).collect(Collectors.toList());
        if (customerIds.contains(friendId)) {
           return true;
        }
        ImFriend imFriend = new ImFriend();
        imFriend.setFriendId(chatId1.getId());
        imFriend.setUserId(chatId.getId());
        imFriend.setState(SysUtils.FRIEND_STATUS_COMMON);
        iImFriendService.save(imFriend);
        return true;
    }
    /**
     * 获取部门下的用户
     *
     * @param deptId 部门id
     * @return List<User>
     */
/*    @Override
    public List<User> getByDept(String deptId) {
        SysUser sysUser = new SysUser();
        sysUser.setDeptId(Long.parseLong(deptId));
        return iSysUserService.selectUserList(sysUser).stream().map(this::transform).collect(Collectors.toList());
    }

    *//**
     * 根据用户mobile获取用户
     *
     * @param mobile mobile
     * @return List<User>
     *//*
    @Override
    public List<User> search(String mobile) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        return iSysUserService.list(queryWrapper).stream().map(this::transform).collect(Collectors.toList());
    }

    *//**
     * 更新用户 同时清空用户相关的缓存
     *
     * @param user 用户
     * @return 更新数
     *//*
    @Override
    @CacheEvict(value = CACHE_KEY + ":one", key = "#user.id")
    public int update(User user) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(Long.parseLong(user.getId()));
        sysUser.setAvatar(user.getAvatar());
        sysUser.setNickName(user.getName());
        sysUser.setEmail(user.getEmail());
        sysUser.setSex(user.getSex());
        return iSysUserService.updateUser(sysUser);
    }


    */

    /**
     * 删除好友同时清除双方的好友缓存关系
     *
     * @param friendId 好友id
     * @param userId   用户id
     * @return boolean
     *//*
    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_KEY + ":friend", key = "#userId"),
            @CacheEvict(value = CACHE_KEY + ":friend", key = "#friendId")
    })
    public boolean delFriends(String friendId, String userId) {
        QueryWrapper<ImFriend> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("friend_id", friendId);
        boolean f1 = iImFriendService.remove(wrapper);

        QueryWrapper<ImFriend> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id", friendId);
        wrapper1.eq("friend_id", userId);
        boolean f2 = iImFriendService.remove(wrapper1);
        return f1 || f2;
    }

    private User transform(SysUser sysUser) {
        return new User(String.valueOf(sysUser.getUserId()), sysUser.getNickName(), sysUser.getAvatar(), sysUser.getMobile(), sysUser.getSex(), String.valueOf(sysUser.getDeptId()), sysUser.getEmail());
    }

    @Override
    public boolean isFriends(String friendId, String userId) {
        QueryWrapper<ImFriend> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("friend_id", friendId);
        boolean f0 = iImFriendService.count(wrapper)==1;

        QueryWrapper<ImFriend> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id", friendId);
        wrapper1.eq("friend_id", userId);
        boolean f1 = iImFriendService.count(wrapper1)==1;

        return f0 || f1;
    }

    @Override
    public int save(User user) {
        SysUser sysUser = new SysUser();
        sysUser.setAvatar(user.getAvatar());
        sysUser.setNickName(user.getName());
        sysUser.setEmail(user.getEmail());
        sysUser.setSex(user.getSex());
        return iSysUserService.updateUser(sysUser);
    }*/
}
