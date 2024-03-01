package com.wcwy.post.api;

import com.wcwy.post.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * ClassName: SendMessageApi
 * Description:
 * date: 2023/10/11 8:57
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-websocket")
public interface SendMessageApi {
    @PostMapping("/sendMessage/sendMsg")
    void sendMsg(@RequestBody Map<String,Object> map);
}
