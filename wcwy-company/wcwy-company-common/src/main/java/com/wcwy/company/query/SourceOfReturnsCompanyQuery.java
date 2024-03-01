package com.wcwy.company.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * ClassName: SourceOfReturnsCompanyQuery
 * Description:
 * date: 2023/7/21 11:54
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("企业+推荐官收益详情")
public class SourceOfReturnsCompanyQuery extends PageQuery {


    @ApiModelProperty("年份(传年份如（2023，2022）)")

   private String year;
    /**
     * 订单完成时间
     */

    private String userId;

    @ApiModelProperty("活推荐官id")
    @NotBlank(message = " 信息不能为空！")
    private String company;
}
