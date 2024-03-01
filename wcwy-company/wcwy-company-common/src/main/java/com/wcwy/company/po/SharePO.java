package com.wcwy.company.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: Share
 * Description:
 * date: 2023/4/6 11:05
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "分成查询")
public class SharePO {

    @ApiModelProperty(value ="目前年薪" )
    private BigDecimal currentSalary;
    @ApiModelProperty(value ="求职者分享注册人" )
    private String sharePerson;
    @ApiModelProperty(value ="推荐官分享注册人" )
    private String recommendShare;
}
