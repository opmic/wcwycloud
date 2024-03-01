package com.wcwy.post.api;

import com.wcwy.common.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: CompanyUserRoleApi
 * Description:
 * date: 2022/10/19 9:01
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-post")
public interface CompanyUserRoleApi {
    @GetMapping("/companyUserRole/selectExamineRole")
    public R<Boolean> selectExamineRole(@RequestParam("companyId") String companyId, @RequestParam("jobhunterId") String jobhunterId);
}
