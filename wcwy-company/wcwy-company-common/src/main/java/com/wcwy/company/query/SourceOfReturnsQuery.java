package com.wcwy.company.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * ClassName: SourceOfReturnsQuery
 * Description:
 * date: 2023/7/21 9:42
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("求职者分享查询")
public class SourceOfReturnsQuery extends PageQuery {

    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    /**
     * 订单完成时间
     */

    private String userId;

    @ApiModelProperty("求职者id")
    @NotBlank(message = "求职者信息不能为空")
    private String jobHunter;
}
