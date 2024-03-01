package com.wcwy.company.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * ClassName: RMDownloadQuery
 * Description:
 * date: 2023/6/5 8:23
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("人才推荐官我的人才下载")
public class RMDownloadQuery extends PageQuery {
    @ApiModelProperty(value ="生日(1:18-25 2:26-30 3:31-35 4:36-40 5:41-45 6：46以上 )" )
    private Integer birthday;

    @ApiModelProperty(value = "人才来源(0:分享引流 1:人才委托 2新增人才 3应聘简历)")
    private Integer correlationType;

    @ApiModelProperty(value ="学历( 2中专/中技 3:高中 4:大专 5:本科 6:硕士 7:博士)" )
    private List<String> education;
    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @ApiModelProperty(value ="关键字" )
    private String keyword;
    @ApiModelProperty(value = "不要传值",required = false)
    Date startTime;
    @ApiModelProperty(value = "不要传值",required = false)
    Date endTime;
}
