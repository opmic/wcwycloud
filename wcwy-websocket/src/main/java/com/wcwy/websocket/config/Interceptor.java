package com.wcwy.websocket.config;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import static org.ietf.jgss.GSSException.UNAUTHORIZED;

/**
 * ClassName: Interceptor
 * Description:
 * date: 2023/3/29 21:14
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

//@Component
public class Interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从header中获取token值
        String token = request.getHeader("token");
        System.out.println("11111111111111111111111111111");
       /* token = StringUtils.isBlank(token) ? request.getParameter("token") : token;
        if (StringUtils.isBlank(token)){
            log.debug("header token is null");
            response.setStatus(UNAUTHORIZED);
            response.sendRedirect("/login");
            return false;
        }*/

        //判断token有效性
      /*  boolean tokenValid = JwtTokenUtils.tokenValid(token);
        if (!tokenValid){
            log.debug("invalid token");
            response.sendRedirect("/login");
            response.setStatus(UNAUTHORIZED);
            return false;
        }

        return true;*/

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
