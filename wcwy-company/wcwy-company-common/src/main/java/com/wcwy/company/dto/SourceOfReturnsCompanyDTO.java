package com.wcwy.company.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * ClassName: SourceOfReturnsCompanyDTO
 * Description:
 * date: 2023/7/21 8:41
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "收益详情企业")
public class SourceOfReturnsCompanyDTO {

    @ApiModelProperty(value ="日期" )
   // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private String mont;

    @ApiModelProperty(value ="总收益" )
    private String totalRevenue;

    @ApiModelProperty(value ="我的收益" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String myEarnings;
}
