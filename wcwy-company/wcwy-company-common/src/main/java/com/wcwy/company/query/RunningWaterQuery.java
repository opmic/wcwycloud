package com.wcwy.company.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * ClassName: RunningWaterQuery
 * Description:
 * date: 2023/6/30 14:52
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("收支明细")
public class RunningWaterQuery  extends PageQuery {

    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;


    @ApiModelProperty("支付类型(0:无忧币 1:金币)")
    private Integer type;

    @ApiModelProperty(" 1:支出 2:收入")
    private Integer ifIncome;

}
