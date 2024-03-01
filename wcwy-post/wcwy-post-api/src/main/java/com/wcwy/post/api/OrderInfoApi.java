package com.wcwy.post.api;

import com.wcwy.post.entity.OrderInfo;
import com.wcwy.post.pojo.DivideIntoPOJO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * ClassName: OrderInfoApi
 * Description:
 * date: 2023/4/10 19:07
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-post")
public interface OrderInfoApi {
    @GetMapping(value = "/orderInfo/divideInto", consumes = "application/json;charset=utf-8", produces = "application/json;charset=utf-8")
    DivideIntoPOJO divideInto(@RequestParam("identityId") String identityId, @RequestParam("recommendId") String recommendId, @RequestParam("type") Integer type);

    @GetMapping("/orderInfo/addOrder")
    Boolean addOrder(@RequestParam("orderInfo") String orderInfo);

    @GetMapping("/orderInfo/postServiceCharge")
    public BigDecimal postServiceCharge(@RequestParam(value = "postId") String postId);
}
