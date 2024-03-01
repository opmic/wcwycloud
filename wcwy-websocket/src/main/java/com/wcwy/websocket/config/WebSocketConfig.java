package com.wcwy.websocket.config;

import com.wcwy.common.config.session.LoginUser;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.util.WebAppRootListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 首先要注入ServerEndpointExporter，这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint。
 * 要注意，如果使用独立的servlet容器，而不是直接使用springboot的内置容器，就不要注入ServerEndpointExporter，
 * 因为它将由容器自己提供和管理。
 */
//@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator implements ServletContextInitializer {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    //配置websocket传输大小，50M
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(WebAppRootListener.class);
        servletContext.setInitParameter("org.apache.tomcat.websocket.textBufferSize","52428800");
        servletContext.setInitParameter("org.apache.tomcat.websocket.binaryBufferSize","52428800");
    }


    /**
     * 建立握手时，连接前的操作
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 这个userProperties 可以通过 session.getUserProperties()获取
        final Map<String, Object> userProperties = sec.getUserProperties();
        Map<String, List<String>> headers = request.getHeaders();
      //  List<String> remoteIp = headers.get("x-forwarded-for");
       // List<String> header1 = headers.get("Authorization");
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // List<String> header2 = headers.get("x2");
        userProperties.put("id", loginUser.getId());
        //userProperties.put("header2", header2.get(0));
       // userProperties.put("x-forwarded-for", remoteIp.get(0));
    }

    /**
     * 初始化端点对象,也就是被@ServerEndpoint所标注的对象
     */
    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return super.getEndpointInstance(clazz);
    }

}
