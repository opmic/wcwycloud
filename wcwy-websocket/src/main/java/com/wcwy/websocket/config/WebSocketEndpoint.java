package com.wcwy.websocket.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.websocket.entity.ChatUser;
import com.wcwy.websocket.service.ChatNewsService;
import com.wcwy.websocket.service.ChatUserService;
import com.wcwy.websocket.utils.SendCodeUtils;
import com.wcwy.websocket.vo.SendInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

//@ServerEndpoint(value = "/websocket",configurator = WebSocketConfig.class)//,configurator = WebSocketConfig.class
//@Component
public class WebSocketEndpoint {

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	private static ChatNewsService chatNewsService;

   private static ChatUserService chatUserService;

   private static RedisUtils redisUtils;
	@Autowired
	public void setActionLogMapper(ChatNewsService chatNewsService,ChatUserService chatUserService,RedisUtils redisUtils) {
		WebSocketEndpoint.chatNewsService = chatNewsService;
		WebSocketEndpoint.chatUserService = chatUserService;
		WebSocketEndpoint.redisUtils = redisUtils;
	}
	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session) {
		String userId1 = getHeader(session, "id");
		System.out.println("用户：" + userId1 + "已连接到websocke服务器");
		//System.out.println(chatNewsService);
	/*	System.out.println("用户：" + userId + "已连接到websocke服务器");

		Object id = session.getUserProperties().get("id");
*/

		// 分解获取的参数,把参数信息，放入到session key中, 以方便后续使用
//		String queryString = session.getQueryString();
//		HashMap<String,String> maps = HttpContextUtils.parseQueryString(queryString);
//		String userId = maps.get("userId");
		// 把会话存入到连接池中
		this.session = session;
		SessionPool.sessions.put(userId1,session);
		SessionPool.add(userId1, session);
	}
	public static String getHeader(Session session, String headerName) {
		final String header = (String) session.getUserProperties().get(headerName);
		if (StrUtil.isBlank(header)) {
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return header;
	}

	/**
	 * 关闭连接
	 */
	@OnClose
	public void onClose(Session session) {
		SessionPool.sessions.remove(session.getId());
	}

	/**ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message
	 *            客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		//System.out.println("来自客户端的消息:" + message);
		String userId = getHeader(session, "id");
		System.out.println(userId);

		SendInfo sendInfo = JSON.parseObject(message, SendInfo.class);
		if (SendCodeUtils.PING.equals(sendInfo.getCode())) {
			//WsResponse wsResponse = WsResponse.fromText(text, TioServerConfig.CHARSET);
			// Tio.send(channelContext, wsResponse);
			session.getBasicRemote().sendText(JSON.toJSONString(message));
		}
		//准备就绪，需要发送离线消息
		else if (SendCodeUtils.READY.equals(sendInfo.getCode())) {
			//未读消息
			session.getBasicRemote().sendText(JSON.toJSONString(message));
		}
		//return null;
	/*	Set<MessageHandler> messageHandlers = session.getMessageHandlers();
		// 如果是心跳检测的消息，则返回pong作为心跳回应
		if (message.equalsIgnoreCase("ping")) {
			try {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("type", "pong");
				session.getBasicRemote().sendText(JSON.toJSONString(params));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else
		{
			Map map = JSON.parseObject(message, Map.class);
			map.put("fromUserId",userId);
			ChatUser add = chatUserService.add(map);
			map.put("chatId",add.getId());
			chatNewsService.addNews(add.getId(),map);
			//redisUtils.setIfAbsent(Cache.CACHE_SOCKET.getKey()+add.getId()+map.get("toUserId"),add.getId(),1);
			redisUtils.del(Cache.CACHE_SOCKET.getKey()+add.getId()+userId);
			redisUtils.del(Cache.CACHE_SOCKET.getKey()+add.getId()+map.get("toUserId"));
			SessionPool.sendMessage(map);
		}*/
	}

}
