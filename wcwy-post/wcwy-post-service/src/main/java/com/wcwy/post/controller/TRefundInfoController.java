package com.wcwy.post.controller;

import com.wcwy.post.service.OrderInfoService;
import com.wcwy.post.service.TRefundInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 * @description 针对表【t_refund_info(退款表)】的数据库操作Mapper
 * @createDate 2022-10-18 15:17:16
 * @Entity com.wcwy.post.entity.TRefundInfo
 */
@RestController
@Api(tags = "退款接口")
@RequestMapping("/tRefundInfo")
public class TRefundInfoController {
    @Autowired
    private TRefundInfoService tRefundInfoService;
    @Autowired
    private OrderInfoService orderInfoService;

  /*  @PreAuthorize("hasAnyAuthority('admin')")
    @ApiOperation("退款审核通知接口")
    @PostMapping("/audit")
    public R audit(@RequestBody TRefundInfoVO tRefundInfoVO) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("order_on",tRefundInfoVO.getOrderNo());
        OrderInfo one = orderInfoService.getOne(queryWrapper);

    }*/
}




