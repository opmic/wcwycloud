package com.wcwy.company.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * ClassName: GraphDTO
 * Description:
 * date: 2023/7/21 15:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel("入驻数量增长")
@Data
public class GraphDTO {
    @ApiModelProperty("求职者")
    private List<Map> jobHunter;
    @ApiModelProperty("推荐官")
    private List<Map> recommend;
    @ApiModelProperty("企业")
    private List<Map> company;
}
