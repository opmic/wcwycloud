package com.wcwy.oauth.config;

/**
 * ClassName: MyResponseBodyAdvice
 * Description:
 * date: 2023/3/28 15:15
 *
 * @author tangzhuo
 * @since JDK 1.8
 */


import com.alibaba.fastjson.JSON;
import com.wcwy.common.base.enums.IpAddress;
import com.wcwy.common.base.result.R;
import com.wcwy.oauth.utlis.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@ControllerAdvice
public class MyResponseBodyAdvice implements ResponseBodyAdvice {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        //此处返回true,表示对任何handler的responsebody都调用beforeBodyWrite方法，如果有特殊方法不使用可以考虑使用注解等方式过滤
        return true;
    }
    @ResponseBody
    //@ExceptionHandler(value = OAuth2Exception.class)
    @ExceptionHandler(value = Exception.class)
    public R handleOauth2(Exception e) {
        //R.success("登录失败！",body)
        return R.error(401,e.getMessage());
    }
    /**
     * 对Controller的所有返回结果进行处理
     * @param body 是controller方法中返回的值，对其进行修改后再return
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Autowired
    private HttpServletRequest request;
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        log.info("请求返回数据类型class="+ body.getClass().getName());
  /*      System.out.println(body);
        System.out.println( body.toString().contains("error"));
        System.out.println( body.toString().contains("401"));*/
        String toJSONString1 = JSON.toJSONString(body);
        Map map1 = JSON.parseObject(toJSONString1, Map.class);
        System.out.println(map1.get("value"));
        if (!StringUtils.isEmpty(map1.get("value"))){
            String toJSONString = JSON.toJSONString(body);
            Map map = JSON.parseObject(toJSONString, Map.class);
            String ip = IpUtil.getIpAddr(request);

            ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
            stringValueOperations.set(IpAddress.login_ip.getIpAddress()+map.get("value"),ip);
            redisTemplate.expire(IpAddress.login_ip.getIpAddress()+map.get("value"), 15, TimeUnit.DAYS);
            return R.success("登录成功！",body);
        }else {
            return body;
          //  return R.error(401,body.toString());
        }


    }
}

