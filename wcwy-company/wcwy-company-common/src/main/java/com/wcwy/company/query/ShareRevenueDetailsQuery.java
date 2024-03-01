package com.wcwy.company.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * ClassName: ShareRevenueDetailsQuery
 * Description:
 * date: 2023/8/11 11:39
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("分享收益查询")
public class ShareRevenueDetailsQuery extends PageQuery {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private Date date;
    @ApiModelProperty("关键字")
    private String keyword;

    private String year;
    private String month;

    @NotNull(message = "请选择身份！")
    @ApiModelProperty("身份(0:企业 1:推荐官 2:求职者)")
    @Max(value = 2,message = "请传入正确的值！")
    @Min(value = 0,message = "请传入正确的值！")
    private Integer identity;

    private String userId;
}
