package com.wcwy.oprnfeign.service;

import com.wcwy.oprnfeign.entiy.SchoolQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * ClassName: TCompanyApi
 * Description:
 * date: 2022/10/13 13:55
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-company")
public interface TCompanyApi {
    @GetMapping("/company/cccs")
    public String cc();


    @PostMapping( value = "/company/asc" , consumes = "application/json")
    public String asc(@RequestBody SchoolQuery schoolQuery);
}
