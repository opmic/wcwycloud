package com.wawy.company.api;

import com.wawy.company.config.FeignConfig;
import com.wcwy.company.entity.TJobhunter;
import com.wcwy.company.entity.TRecommend;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * ClassName: TRecommendApi
 * Description:
 * date: 2023/6/19 8:40
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-company",configuration = FeignConfig.class)
public interface TRecommendApi {

    @GetMapping("/tRecommend/selects")
    public List<TRecommend> selects();
    @GetMapping("/tRecommend/getSharePersonRecommend")
    public Map<String,Object> getSharePersonRecommend(@RequestParam("id") String id);
    @GetMapping("/tRecommend/deductCurrency")
     Map<String, Object> deductCurrency(@RequestParam("userId") String userId, @RequestParam("jobHunter") String jobHunter, @RequestParam("orderId") String orderId);

    @GetMapping("/tRecommend/selectId")
    TRecommend selectId(@RequestParam("id") String id);

    @GetMapping("/tRecommend/lists")
     List<TRecommend> lists(@RequestParam("list") List<String> list);
}
