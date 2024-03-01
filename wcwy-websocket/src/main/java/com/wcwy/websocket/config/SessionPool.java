package com.wcwy.websocket.config;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.util.RedisUtils;
/*import com.wcwy.websocket.session.CompanyMetadata;*/
import com.wcwy.websocket.entity.ChatUser;
import com.wcwy.websocket.service.ChatNewsService;
import com.wcwy.websocket.service.ChatUserService;
import com.wcwy.websocket.service.impl.ChatNewsServiceImpl;
import com.wcwy.websocket.service.impl.ChatUserServiceImpl;
import com.wcwy.websocket.session.CompanyMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;


public class SessionPool {

    public static Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

    private static String msg = "websocket:";

    	/*private CompanyMetadata companyMetadata=WebsocketApplicationContextAware.getApplicationContext().getBean("companyMetadata",CompanyMetadata.class);*/

    private static RedisUtils redisUtils = WebsocketApplicationContextAware.getApplicationContext().getBean(RedisUtils.class);
   /* private static ChatUserServiceImpl chatUserService = WebsocketApplicationContextAware.getApplicationContext().getBean("chatUserServiceImpl", ChatUserServiceImpl.class);*/
    private ThreadPoolExecutor dtpExecutor1 = WebsocketApplicationContextAware.getApplicationContext().getBean("dtpExecutor1", ThreadPoolExecutor.class);

/*    private static ChatNewsServiceImpl chatNewsService = WebsocketApplicationContextAware.getApplicationContext().getBean("chatUserServiceImpl", ChatNewsServiceImpl.class);*/

    public static void close(String sessionId) throws IOException {
        Session session = sessions.get(sessionId);
        if (session != null) {
            sessions.get(sessionId).close();
        }

    }

    public static void add(String userId, Session session) {
        SessionPool sessionPool = new SessionPool();
        //sessions.put(userId,session);
        sessionPool.msg(userId);
    }

    public void msg(String userId) {
        dtpExecutor1.execute(() -> {
            List<Object> objects = redisUtils.msgGetList(msg + userId);
            if (objects != null && objects.size() > 0) {
                for (Object object : objects) {
                    try {
                        SessionPool.sendMessageByUserId(userId, object.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void sendMessage(String sessionId, String message) {
        sessions.get(sessionId).getAsyncRemote().sendText(message);
    }

    public static void sendMessage(String message) {
        for (String sessionId : SessionPool.sessions.keySet()) {
            SessionPool.sessions.get(sessionId).getAsyncRemote().sendText(message);
        }
    }

    public static void sendMessageByUserId(String userId, String message) throws IOException {
        Session session = SessionPool.sessions.get(userId);
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText(message);

        } else {
            redisUtils.msgAddList(msg + userId, message);
        }
    }

    public static String sendMessage(Map<String, Object> params) throws IOException {
        String toUserId = params.get("toUserId").toString();
        params.put("time", LocalDateTime.now());
        String s = JSON.toJSONString(params);
        sendMessageByUserId(toUserId, s);
        return "发送成功";
    }
}
