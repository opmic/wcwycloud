package com.wcwy.websocket.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * ClassName: WebsocketApplictionContextAware
 * Description:
 * date: 2023/10/11 10:31
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
//@Component
//@Lazy(value = false)
public class WebsocketApplicationContextAware implements ApplicationContextAware {

    private static ApplicationContext APPLICATION_CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT=applicationContext;
    }
    public static ApplicationContext getApplicationContext(){
        return APPLICATION_CONTEXT;
    }
}
