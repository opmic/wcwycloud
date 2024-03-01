package com.wawy.company.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


/**
 * ClassName: CollerctPostController
 * Description:
 * date: 2022/10/14 10:50
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-company")
public interface CollerctPostApi {
    @GetMapping("/collerct/isCollect")
     boolean isCollect(@RequestParam("jobHunter") String jobHunter,@RequestParam("postId") String postId);
}
