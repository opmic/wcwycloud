
package com.wcwy.company.config;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wcwy.common.redis.enums.Message;
import com.wcwy.common.redis.enums.Sole;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @ClassName WebSocketServer
 * @Description: //TODO
 * @Author wyq
 * @Date 2022/5/5 17:47
 * @ServerEndpoint 通过这个 spring boot 就可以知道你暴露出去的 websockst 应用的路径，有点类似我们经常用的@RequestMapping。比如你的启动端口是8080，而这个注解的值是ws，那我们就可以通过 ws://127.0.0.1:8080/websocket 来连接你的应用
 * @OnOpen 当 websocket 建立连接成功后会触发这个注解修饰的方法，注意它有一个 Session 参数
 * @OnClose 当 websocket 建立的连接断开后会触发这个注解修饰的方法，注意它有一个 Session 参数
 * @OnMessage 当客户端发送消息到服务端时，会触发这个注解修改的方法，它有一个 String 入参表明客户端传入的值
 * @OnError 当 websocket 建立连接时出现异常会触发这个注解修饰的方法，注意它有一个 Session 参数
 */

//@Component
//@ServerEndpoint("/websocket/{userId}")
public class WebSocketServer {


    /**
     * concurrent包的线程安全Set，用来存放每个用户对应的Session对象。
     */
// 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private RedisTemplate redisTemplate =  SessionPool.redisTemplate;

    /**
     * 连接建立成功调用的方法
     */

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) throws IOException {
        System.out.println("用户：" + userId + "已连接到websocke服务器");
        if (userId == null) {
            return;
        }
        this.session = session;
        SessionPool.sessions.put(userId, session);

        SetOperations<String, String> setOperations = redisTemplate.opsForSet();//存储
        SetOperations<String, String> setOperationsMsg = redisTemplate.opsForSet();
        Long size = setOperations.size(Sole.MESSAGE.getKey() + userId);
        //查看redis缓存是否有有消息缓存  如果有就发给当事人
        String substring = userId.substring(0, 2);
        if (size > 0) {
            Set<String> members = setOperations.members(Sole.MESSAGE.getKey() + userId);
            for (String s : members) {
                Set<String> members1=null;
                if ("TC".equals(substring)) {
                    members1 = setOperationsMsg.members(Message.COMPANY_INTERVIEW.getKey() + s);
                } else if ("TJ".equals(substring) || "TR".equals(substring)) {
                    members1 = setOperationsMsg.members(Message.REFERRER_INTERVIEW.getKey() + s);
                } else {
                    members1 = setOperationsMsg.members(Sole.MESSAGE.getKey() + s);
                }
                for (String s1 : members1) {
                    SessionPool.sendMessageByUserId(userId,s1);
                }
            }
        }

    }


    /**
     * 连接关闭调用的方法
     */

    @OnClose
    public void onClose(@PathParam("userId") String userId) throws IOException {
        System.out.println("用户：" + userId + "已离开websocket服务器");
        SessionPool.close(userId);
    }

    /**
     * 收到客户端消息后调用的方法
     */

    @OnMessage
    public void onMessage(String json,Session session) throws IOException {
        // 如果是心跳检测的消息，则返回pong作为心跳回应
        if (json.equalsIgnoreCase("ping")) {
            try {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", "pong");
                session.getBasicRemote().sendText(JSON.toJSONString(params));
               // System.out.println("应答客户端的消息:" + JSON.toJSONString(params));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        else
        {
            SetOperations<String, String> setOperations = redisTemplate.opsForSet();//存储
            SetOperations<String, String> setOperationsMsg = redisTemplate.opsForSet();
            System.out.println("前端发送的信息为：" + json);
            JSONObject jsonObject = JSON.parseObject(json);
            String user = (String) jsonObject.get("sendId");//发送人
            String acceptId = (String) jsonObject.get("acceptId");//接受人
            String putInResumeId = (String) jsonObject.get("putInResumeId");//投简id
            String msg = (String) jsonObject.get("msg");//消息
            SessionPool.sendMessageByUserId(acceptId,json);
            //进行redis存储
            setOperations.add(Sole.MESSAGE.getKey() + acceptId, acceptId+putInResumeId);
            String substring = acceptId.substring(0, 2);
            if ("TC".equals(substring)) {
                setOperationsMsg.add(Message.COMPANY_INTERVIEW.getKey()+acceptId + putInResumeId,json);
            } else if ("TJ".equals(substring) || "TR".equals(substring)) {
                setOperationsMsg.add(Message.REFERRER_INTERVIEW.getKey()+acceptId + putInResumeId,json);
            } else {
                setOperationsMsg.add(Sole.MESSAGE.getKey() +acceptId+ putInResumeId,json);
            }


        }

    }


    /**
     * 出现异常触发的方法
     */

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 单发给某人
     */

 /*   public void sendMessageTo(String message, Session session) throws IOException {
        session.getBasicRemote().sendText(message);
    }*/

}
