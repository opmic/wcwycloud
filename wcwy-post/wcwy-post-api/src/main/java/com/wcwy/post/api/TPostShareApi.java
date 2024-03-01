package com.wcwy.post.api;

import com.wcwy.post.dto.TotalPostShare;
import com.wcwy.post.entity.TPostShare;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: TPostShareApi
 * Description:
 * date: 2022/11/11 8:27
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-post")
public interface TPostShareApi {

    @GetMapping("/tPostShare/select")
     TPostShare select(@RequestParam("companyPostId") String companyPostId);
    @GetMapping("/tPostShare/selectTotalPostShare")
     TotalPostShare selectTotalPostShare(@RequestParam("companyId") String companyId);
}
