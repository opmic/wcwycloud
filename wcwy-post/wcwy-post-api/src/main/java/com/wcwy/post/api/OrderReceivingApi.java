package com.wcwy.post.api;

import com.wcwy.company.po.PutPostShareDataPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * ClassName: OrderReceivingApi
 * Description:
 * date: 2023/5/25 10:17
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-post")
public interface OrderReceivingApi {

    @GetMapping("/orderReceiving/selectCount")
     Map<String,Integer> selectCount(@RequestParam("rmUserId") String rmUserId);
    @GetMapping("/orderReceiving/orderReceivingDay")
     Map<String, List<PutPostShareDataPO>> orderReceivingDay(@RequestParam("id") String id, @RequestParam("localDate") LocalDate localDate,@RequestParam(value = "city",required = false) String city,@RequestParam(value = "postType") Integer postType);
    @GetMapping("/orderReceiving/orderReceivingWeek")
    public Map<String, List<PutPostShareDataPO>> orderReceivingWeek(@RequestParam("id") String id, @RequestParam("beginDate") LocalDate beginDate,@RequestParam("allEndDate") LocalDate allEndDate,@RequestParam(value = "city",required = false) String city,@RequestParam(value = "postType") Integer postType) ;
    @GetMapping("/orderReceiving/orderReceivingMonth")
    public Map<String, List<PutPostShareDataPO>> orderReceivingMonth(@RequestParam("id") String id, @RequestParam("beginDate") LocalDate beginDate,@RequestParam("allEndDate") LocalDate allEndDate,@RequestParam(value = "city",required = false) String city,@RequestParam(value = "postType") Integer postType) ;

    }
