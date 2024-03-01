package com.wawy.company.api;

import com.wawy.company.config.FeignConfig;
import com.wcwy.company.entity.TJobhunter;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * ClassName: TJobHunterApi
 * Description:
 * date: 2023/4/4 8:26
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-company",configuration = FeignConfig.class)
public interface TJobHunterApi {

    /**
     * 获取求职者的现目前薪资及邀请的推荐官及推荐官邀请的推荐官
     * @param jonHunter
     * @return
     */
    @GetMapping("/jobhunter/getCurrentSalary")
    Map<String,Object> getCurrentSalary(@RequestParam("jonHunter") String jonHunter);

    @GetMapping(value = "/jobhunter/getSharePersonRecommend")
     Map<String,String> getSharePersonRecommend(@RequestParam("jobHunterId") String jobHunterId);

    @GetMapping(value = "/jobhunter/payResume")
     Map<String, Object> payResume(@RequestParam("jobHunter") String jobHunter,@RequestParam("paymentType") Integer paymentType,@RequestParam("postType") Integer postType);


    @GetMapping(value = "/jobhunter/selectId")
    TJobhunter selectId(@RequestParam("jobHunter") String jobHunter);

    @GetMapping("/jobhunter/lists")
    public List<TJobhunter> lists(@RequestParam("list") List<String> list);
}
