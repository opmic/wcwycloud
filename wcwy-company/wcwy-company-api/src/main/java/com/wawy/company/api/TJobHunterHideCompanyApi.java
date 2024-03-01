package com.wawy.company.api;

import com.wawy.company.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * ClassName: TJobHunterHideCompanyApi
 * Description:
 * date: 2023/8/8 14:35
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-company", configuration = FeignConfig.class)
public interface TJobHunterHideCompanyApi {

    @GetMapping("/jobHunterHideCompany/cacheCompany")
    public List<Object> cacheCompany(@RequestParam("jobHunter") String jobHunter);
}
