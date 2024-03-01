package com.wcwy.websocket.controller;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.websocket.config.SessionPool;
import com.wcwy.websocket.entity.ChatUser;
import com.wcwy.websocket.entity.Message;
import com.wcwy.websocket.service.ChatNewsService;
import com.wcwy.websocket.service.ChatUserService;
import com.wcwy.websocket.service.ImMessageService;
import com.wcwy.websocket.service.impl.ChatUserServiceImpl;
import com.wcwy.websocket.tio.TioWebsocketStarter;
import com.wcwy.websocket.tio.TioWsMsgHandler;
import com.wcwy.websocket.tio.WsOnlineContext;
import com.wcwy.websocket.vo.SendInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.tio.client.ClientChannelContext;
import org.tio.core.ChannelContext;
import org.tio.core.Node;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.utils.hutool.Snowflake;
import org.tio.websocket.common.WsResponse;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ClassName: SendMessageController
 * Description:
 * date: 2023/10/10 16:44
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RequestMapping("/sendMessage")
@RestController
@Api(tags = "发送消息测试")
@Slf4j
public class SendMessageController {

    @Resource
    private ThreadPoolExecutor dtpExecutor1;
    @Autowired
    private ImMessageService vimMessageService;
    @Resource
  private   TioWsMsgHandler tioWsMsgHandler;
    @PostMapping("/sendMsg")
    @ApiOperation("发送信息")
    public void sendMsg(@RequestBody String text) throws IOException {
        if(StringUtils.isEmpty(text)){
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        SendInfo sendInfo = objectMapper.readValue(text, SendInfo.class);
        Map<String, Object> map = sendInfo.getMessage();
        map.put("mine", false);
        map.put("timestamp", System.currentTimeMillis());
        map.put("id", String.valueOf(new Snowflake(1L, 1L).nextId()));
        Message message = BeanUtil.fillBeanWithMap(map, new Message(), false);
        ChannelContext channelContextByUser = WsOnlineContext.getChannelContextByUser(message.getChatId());



     /*   TioConfig serverTioConfig = channelContextByUser.tioConfig;
        WsResponse wsResponse = WsResponse.fromText("服务器已收到消息：", "UTF-8");
        Boolean aBoolean = Tio.sendToUser(serverTioConfig, "1", wsResponse);*/

    try {

        if(channelContextByUser==null){

            vimMessageService.save(message, false);
            return;
        }
        tioWsMsgHandler.acceptMessage(channelContextByUser,objectMapper,sendInfo);
    }catch (Exception e){
        log.error("发送消息失败=》"+e);
    }

    }
}
