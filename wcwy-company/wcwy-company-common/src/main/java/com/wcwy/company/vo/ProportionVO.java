package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: ProportionVO
 * Description:
 * date: 2022/10/19 9:50
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class ProportionVO {
    /**
     * 企业id
     */
    private String  tCompanyId;
    /**
     * 求职者id
     */
    private String jobhunterId;

    /**
     *无忧币分成金额
     */
    private BigDecimal money;

    /**
     *1下载简历 2 保证金及赏金结算
     */
    private int identification;
    /**
     * 推荐官id
    **/
    private String tRecommend;

    /**
     * 来源订单id
     */
    private String orderId;
}
