package com.wcwy.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.websocket.entity.ImGroup;
import com.wcwy.websocket.entity.ImGroupUser;
import com.wcwy.websocket.mapper.ImGroupUserMapper;
import com.wcwy.websocket.service.ImGroupService;
import com.wcwy.websocket.mapper.ImGroupMapper;
import com.wcwy.websocket.tio.StartTioRunner;
import com.wcwy.websocket.vo.Group;
import com.wcwy.websocket.vo.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tio.core.Tio;
import org.tio.server.ServerTioConfig;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【im_group】的数据库操作Service实现
* @createDate 2023-12-25 11:40:28
*/
@Service
public class ImGroupServiceImpl extends ServiceImpl<ImGroupMapper, ImGroup>
    implements ImGroupService{
    private static final String CACHE_KEY = "group";
    @Resource
    private ImGroupMapper imGroupMapper;
    @Resource
    private ImGroupUserMapper imGroupUserMapper;

    @Resource
    private ApplicationContext applicationContext;
    /**
     * 获取用户所在的群
     *
     * @param userId 用户id
     * @return List<Group>
     */
    @Override
    @Cacheable(value = CACHE_KEY + ":list", key = "#userId")
    public List<Group> getGroups(String userId) {
        List<Group> collect = imGroupMapper.getUserGroups(userId).stream().map(this::transform).collect(Collectors.toList());
        return collect;
    }
    /**
     * 群类型转换
     *
     * @param imGroup 群
     * @return Group
     */
    private Group transform(ImGroup imGroup) {
        return new Group(imGroup.getId(), imGroup.getName(), imGroup.getAvatar(), String.valueOf(imGroup.getMaster()), imGroup.getNeedCheck());
    }


    /*
    */
/**
     * 获取群里所有的用户
     *
     * @param groupId 群id
     * @return List<User>
     *//*

    @Override
    @Cacheable(value = CACHE_KEY + ":user:list", key = "#groupId")
    public List<User> getUsers(String groupId) {
        return imGroupMapper.getGroupUsers(groupId).stream().map(this::transform).collect(Collectors.toList());
    }

    */
/**
     * 获取群
     *
     * @param groupId 群id
     * @return Group
     *//*

    @Override
    @Cacheable(value = CACHE_KEY + ":one", key = "#groupId")
    public Group get(String groupId) {
        ImGroup group = imGroupMapper.selectById(groupId);
        if (group == null) {
            throw new RuntimeException("不能找到对应的群，可能群已经被删除");
        }
        return transform(group);
    }

    */
/**
     * 给群新增用户
     *
     * @param groupId 群id
     * @param userIds 用户id，多个
     * @return 是否添加成功
     *//*

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_KEY + ":user:list", key = "#groupId")
    public boolean addUsers(String groupId, String[] userIds) {
        StartTioRunner startTioRunner = applicationContext.getBean(StartTioRunner.class);
        ServerTioConfig serverTioConfig = startTioRunner.getAppStarter().getWsServerStarter().getServerTioConfig();
        for (String userId : userIds) {
            ImGroupUser imGroupUser = new ImGroupUser();
            imGroupUser.setUserId(userId);
            imGroupUser.setGroupId(groupId);
            imGroupUser.setState("0");
            imGroupUserMapper.insert(imGroupUser);
            Tio.bindGroup(serverTioConfig, userId, groupId);
        }
        return true;
    }

    */
/**
     * 删除群用户
     *
     * @param groupId 群id
     * @param userIds 用户id，多个
     * @return 是否删除成功
     *//*

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CACHE_KEY + ":one", CACHE_KEY + ":list", CACHE_KEY + ":user:list"}, allEntries = true)
    public boolean delUsers(String groupId, String[] userIds) {
        StartTioRunner startTioRunner = applicationContext.getBean(StartTioRunner.class);
        ServerTioConfig serverTioConfig = startTioRunner.getAppStarter().getWsServerStarter().getServerTioConfig();
        for (String userId : userIds) {
            QueryWrapper<ImGroupUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            queryWrapper.eq("group_id", groupId);
            imGroupUserMapper.delete(queryWrapper);
            Tio.unbindGroup(serverTioConfig, userId, groupId);
        }
        return true;
    }

    */
/**
     * 删除一个群
     *
     * @param groupId 群Id
     * @return 删除数量
     *//*

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CACHE_KEY + ":one", CACHE_KEY + ":list", CACHE_KEY + ":user:list"}, allEntries = true)
    public int del(String groupId) {
        this.delUsers(groupId, getUsers(groupId).stream().map(User::getId).toArray(String[]::new));
        return imGroupMapper.deleteById(groupId);
    }
*/




    /**
     * 用户类型转换
     *
     * @param sysUser 用户
     * @return 群
     */
 /*   private User transform(SysUser sysUser) {
        return new User(String.valueOf(sysUser.getUserId()), sysUser.getNickName(), sysUser.getAvatar(), sysUser.getMobile(), sysUser.getSex(), String.valueOf(sysUser.getDeptId()), sysUser.getEmail());
    }*/

    /**
     * 新建一个群
     *
     * @param group 群
     * @return Group
     */
/*    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CACHE_KEY + ":one", CACHE_KEY + ":list", CACHE_KEY + ":user:list"}, allEntries = true)
    public Group save(Group group) {
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        ImGroup imGroup = new ImGroup();
        imGroup.setMaster(sysUser.getUserId());
        imGroup.setName(group.getName());
        imGroup.setAvatar(group.getAvatar());
        imGroup.setDelFlag(SysUtils.DEL_NO);
        imGroup.setNeedCheck(group.getNeedCheck());
        imGroup.preInsert();
        imGroupMapper.insert(imGroup);
        String[] userIds = {String.valueOf(sysUser.getUserId())};
        this.addUsers(imGroup.getId(), userIds);
        group.setId(imGroup.getId());
        return transform(imGroup);
    }*/

    /**
     * 更新一个群
     *
     * @param group 群
     * @return Group
     */
/*    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CACHE_KEY + ":one", CACHE_KEY + ":list", CACHE_KEY + ":user:list"}, allEntries = true)
    public int update(Group group) {
        ImGroup imGroup = imGroupMapper.selectById(group.getId());
        imGroup.setName(group.getName());
        imGroup.setAvatar(group.getAvatar());
        imGroup.setNeedCheck(group.getNeedCheck());
        imGroup.setDelFlag(SysUtils.DEL_NO);
        imGroup.preUpdate();
        return imGroupMapper.updateById(imGroup);
    }*/
}




