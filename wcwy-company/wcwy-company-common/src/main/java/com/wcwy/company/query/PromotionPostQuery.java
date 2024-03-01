package com.wcwy.company.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * ClassName: PromotionPostQuery
 * Description:
 * date: 2023/8/29 17:13
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("推广职位查询")
public class PromotionPostQuery extends PageQuery {

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(value = "推荐官ID",required = false)
    private String recommend ;
}
