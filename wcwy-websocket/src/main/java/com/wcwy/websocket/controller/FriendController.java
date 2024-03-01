package com.wcwy.websocket.controller;

import com.wcwy.common.base.result.R;
import com.wcwy.websocket.entity.ChatId;
import com.wcwy.websocket.entity.Message;
import com.wcwy.websocket.exception.VimBaseException;
import com.wcwy.websocket.service.ChatIdService;
import com.wcwy.websocket.service.ImMessageService;
import com.wcwy.websocket.service.VimUserApiService;
import com.wcwy.websocket.session.CompanyMetadata;
import com.wcwy.websocket.utils.AjaxResult;
import com.wcwy.websocket.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 乐天
 */
@RestController
@RequestMapping("/api/sys/friends")
@Api(tags = "获取聊天人员")
public class FriendController {

    @Resource
    private VimUserApiService vimUserApiService;
    @Resource
    private ImMessageService vimMessageService;

    @Resource
    private ChatIdService chatIdService;

    @Resource
    private CompanyMetadata companyMetadata;

    @GetMapping
    @ApiOperation("获取自己的聊天人员")
    public R<User> list() {
        ChatId userId = chatIdService.getUserId(companyMetadata.userid());
        List<User> friends = vimUserApiService.getFriends(String.valueOf(companyMetadata.userid()));
        for (User friend : friends) {
            List<Message> messageVoList = vimMessageService.list(friend.getId(), userId.getId().toString(), "0", 1L,0L);
            if(messageVoList==null || messageVoList.size()==0){
                continue;
            }
            friend.setMessage(messageVoList.get(0));
        }
        return R.success(friends);
    }


    @GetMapping(value = "add")
    @ApiOperation("添加好友")
    @ApiImplicitParam(name = "friendId", required = true, value = "好友id")
    public AjaxResult add(@RequestParam("friendId") String friendId){
        try {
            return  AjaxResult.success(vimUserApiService.addFriends(friendId,String.valueOf(companyMetadata.userid())));
        } catch (VimBaseException e) {
            throw new VimBaseException("好友已存在！",null);
        }catch (Exception e) {
            throw new VimBaseException("添加好友失败！",null);
        }
    }

/*    @GetMapping(value = "isFriend")
    public AjaxResult isFriend(String friendId){
        SysUser user = SecurityUtils.getLoginUser().getUser();
        return  AjaxResult.success(vimUserApiService.isFriends(String.valueOf(user.getUserId()),friendId));
    }



    @DeleteMapping(value = "delete")
    public AjaxResult delete(@RequestBody String friendId){
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        return  AjaxResult.success(vimUserApiService.delFriends(friendId,String.valueOf(sysUser.getUserId())));
    }*/
}
