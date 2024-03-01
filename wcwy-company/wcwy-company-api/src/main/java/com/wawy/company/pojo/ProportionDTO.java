package com.wawy.company.pojo;


import com.wcwy.post.entity.RunningWater;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * ClassName: ProportionVO
 * Description:购买简历数据实体类
 * date: 2022/10/19 9:50
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class ProportionDTO {
    /**
     * 企业id
     */
    private String  tCompanyId;
    /**
     * 推荐官id
     */
    private String tRecommend;

    /**
     *总金额
     */
    private BigDecimal totalMoney;
    /**
     * 分享者
     */
    private String sharer;
    /**
     * 推荐官分成
     */
    private BigDecimal referrerMoney;
    /**
     * 分享者分成
     */
    private BigDecimal sharerMoney;

    /**
     * 平台分成
     */
    private BigDecimal platformMoney;

    /**
     * 交易状态 （tuer 成功 fales 失败）
     */
    private Boolean paySuccess;
    /**
     * 交易状态失败原因
     */
    private String cause;

    List<RunningWater> list;
}
