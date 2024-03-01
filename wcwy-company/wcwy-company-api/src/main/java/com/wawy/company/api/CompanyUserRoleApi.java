package com.wawy.company.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: CompanyUserRoleApi
 * Description:
 * date: 2023/4/3 20:32
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-company")
public interface CompanyUserRoleApi {

    @GetMapping("/companyUserRole/exist")
     Boolean exist(@RequestParam("userId") String userId, @RequestParam("companyId") String companyId);
}
