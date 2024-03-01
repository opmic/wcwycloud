package com.wcwy.post.query;

import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * ClassName: OrderReceivingQuery
 * Description:
 * date: 2023/5/25 11:10
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("接单大厅")
public class OrderReceivingQuery  extends PageQuery {
    @ApiModelProperty(value = "职位类别")
    private List<String> position;
    /**
     * 工作城市
     */
    @ApiModelProperty(value = "工作城市")
    private List<String> city;
    @ApiModelProperty(value = "关键字")
    private String keyword;
    @ApiModelProperty(value = "岗位选择(0:全部岗位 1:接单岗位,2代办岗位)")
    @NotNull(message = "岗位选择不能为空！")
    private Integer post;

    @ApiModelProperty(value = "企业id")
    private String companyId;
    @ApiModelProperty("求职者id")
    private String jobHunter;

    @ApiModelProperty("岗位状态(0：停止招聘1:招聘中)")
    private Integer status;
    @ApiModelProperty(value = "0:不限 1:3万以下 2:3-5万 3:5-10万 4:10-20万 5:20-50万 6:50万以上")
    private Integer annualSalary;
    @ApiModelProperty(value = "不需要传值")
    private BigDecimal beginSalary;
    @ApiModelProperty(value = "不需要传值")
    private BigDecimal endSalary;
}
