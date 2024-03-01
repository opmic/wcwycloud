package com.wcwy.websocket.controller;

import com.alibaba.fastjson.JSON;
import com.wcwy.websocket.entity.Message;
import com.wcwy.websocket.service.ImMessageService;
import com.wcwy.websocket.utils.AjaxResult;
import com.wcwy.websocket.utils.ChatUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: MessageController
 * Description:
 * date: 2023/12/26 11:26
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/api/sys/messages")
@Api(tags = "聊天信息")
public class MessageController {
    @Resource
    private ImMessageService vimMessageService;

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping
    @ApiOperation("获取聊天信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "chatId", required = true, value = "聊天id"),
            @ApiImplicitParam(name = "fromId", required = true, value = "发送人id"),
            @ApiImplicitParam(name = "type", required = true, value = "0:单聊"),
            @ApiImplicitParam(name = "pageSize", required = true, value = "条数"),
            @ApiImplicitParam(name = "pageNo", required = true, value = "开始")

    })
    public AjaxResult list(@RequestParam("chatId") String chatId,@RequestParam("fromId") String fromId, @RequestParam("type") String type,@RequestParam("pageSize") Long pageSize,@RequestParam("pageNo") Long pageNo) {
        List<Message> messageVoList = vimMessageService.list(chatId, fromId, type, pageSize,pageNo);
        if(messageVoList==null){
            return AjaxResult.success(null);
        }
        for (Message message : messageVoList) {
            message.setMine(fromId.equals(String.valueOf(message.getFromId())));
        }
        String key = ChatUtils.getChatKey(fromId, chatId, type);
        Long size = redisTemplate.opsForZSet().size(key);
        Map map=new HashMap();
        map.put("data",messageVoList);
        map.put("size", size);
        return AjaxResult.success(map);
    }



}
