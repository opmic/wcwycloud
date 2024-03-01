package com.wcwy.oprnfeign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: WokerApi
 * Description:
 * date: 2022/9/13 14:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-post")
@RequestMapping("/work")
public interface WorkApi {

    @GetMapping("/cc")
    public String cc(@RequestParam("cc") String cc);
}
