package com.wawy.company.config;

/**
 * ClassName: FeignConfig
 * Description:
 * date: 2023/4/21 14:51
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description feign客户端配置-设置自定义请求头
 * @ClassName FeignConfig
 * @Author 康世行
 * @Date 11:24 2022/6/23
 * @Version 1.0
 **/
@Configuration
public class FeignConfig implements RequestInterceptor {

    @Value("${ip}")
    public String address;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if(RequestContextHolder.getRequestAttributes() ==null){
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(accessToken ==null){
            HttpClientUtil instance = HttpClientUtil.getInstance();
            String s = instance.sendHttpPost("http://"+address+":9001/oauth/oauth/token","client_id=c1&client_secret=wcwy&username=system&password=wcwy888888&grant_type=admin_password");
            JSONObject jsonObject = JSON.parseObject(s);
            if(! StringUtils.isEmpty(jsonObject.get("data"))){
                JSONObject data = JSON.parseObject(jsonObject.get("data").toString());
                System.out.println(data.get("access_token"));
                requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer "+data.get("access_token"));
                requestTemplate.header("system","system");
            }
        }



    }
}

