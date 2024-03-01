package com.wcwy.oprnfeign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ClassName: TCompanyLoginApi
 * Description:
 * date: 2022/9/13 19:26
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-company")

public interface TCompanyLoginApi {
    @GetMapping("/login/cccs")
    public String cc();
}
