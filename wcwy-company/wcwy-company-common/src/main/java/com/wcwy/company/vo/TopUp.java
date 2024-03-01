package com.wcwy.company.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: TopUp
 * Description:
 * date: 2022/10/17 15:18
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class TopUp {
    /**
     * 用户id
     */
    private String userId;

    /*
    * 充值金额
    * */

    private BigDecimal money;

    /**
     * 订单
     */
    private String order;

}
