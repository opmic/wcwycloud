package com.wcwy.post.api;

import com.wcwy.post.config.FeignConfig;
import com.wcwy.post.entity.TPayConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ClassName: TPayConfigApi
 * Description:
 * date: 2023/7/5 13:35
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-post")
public interface TPayConfigApi {

    @GetMapping("/tRefundInfo/select")
    public TPayConfig select();
}
