package com.wawy.company.api;

import com.wcwy.common.base.result.R;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * ClassName: TCompanyContractApi
 * Description:
 * date: 2023/4/8 9:16
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-company")
public interface TCompanyContractApi {
    @GetMapping("/companyContract/passTheAudit")
    public Map<String,Object> passTheAudit();
}
