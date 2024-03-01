package com.wcwy.post.api;

import com.wcwy.post.entity.ResumePaymentConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * ClassName: ResumePaymentConfigApi
 * Description:
 * date: 2023/9/8 13:53
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-post")
public interface ResumePaymentConfigApi {

    @GetMapping("/resumePaymentConfig/cache")
    List<ResumePaymentConfig> cache();
}
