package com.wcwy.post.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: DivideIntoPOJO
 * Description:
 * date: 2023/4/10 18:48
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "分成记录")
public class DivideIntoPOJO {
    @ApiModelProperty(value = "下载次数")
    private Long download;
    @ApiModelProperty(value = "总费用")
    private BigDecimal costs;
    @ApiModelProperty(value = "下载次数")
    private BigDecimal earnings;
}
