package com.wawy.company.api;


import com.wawy.company.config.FeignConfig;
import com.wcwy.company.vo.TopUp;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * ClassName: TopUpController
 * Description:
 * date: 2022/10/17 15:17
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-company")
public interface TopUpApi {
    @PostMapping("/topUp/updateCurrencyCount")
     Boolean updateCurrencyCount(@RequestBody TopUp topUp) ;
}
