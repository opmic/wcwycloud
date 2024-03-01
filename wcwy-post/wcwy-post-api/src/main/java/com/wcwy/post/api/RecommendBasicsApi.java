package com.wcwy.post.api;

import com.wcwy.post.config.FeignConfig;
import com.wcwy.post.entity.RecommendBasics;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: RecommendBasicsApi
 * Description:
 * date: 2023/6/19 9:31
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-post")
public interface RecommendBasicsApi {
    @PostMapping("/recommendBasics/save")
     void  save(@RequestBody  RecommendBasics recommendBasics);

    @GetMapping("/recommendBasics/administrator")
    public void  administrator(@RequestParam("recommend") String recommend, @RequestParam("firmName") String firmName);
}
