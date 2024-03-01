package com.wawy.company.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: ReferrerRecordApi
 * Description:
 * date: 2023/7/19 10:27
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-company")
public interface ReferrerRecordApi {
    @GetMapping("/referrerRecord/exist")
    Boolean exist(@RequestParam("johHunter") String johHunter);
}
