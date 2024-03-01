package com.wcwy.company.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * ClassName: SelectCompanyResume
 * Description:
 * date: 2023/4/3 10:10
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "公司查询投简")
public class SelectCompanyResumeQuery extends PageQuery {
    /**
     * 学历
     */
    @ApiModelProperty(value ="学历( 2中专/中技 3:高中 4:大专 5:本科 6:硕士 7:博士)" )
    private List<String> education;
    /**
     * 是的代投(0:不是 1:是)
     */
    @ApiModelProperty("岗位id")
    private String postId;
    /**
     * 是的代投(0:不是 1:是)
     */
    @ApiModelProperty("人才来源(0:求职者 1:推荐官)")
    private String easco;
    /**
     * 生日
     */
    @ApiModelProperty(value ="生日(1:18-25 2:26-30 3:31-35 4:36-40 5:41-45 6：46以上 )" )
    private Integer birthday;
    @ApiModelProperty(value ="关键字" )
    private String keyword;


   // @ApiModelProperty("投放状态(1:未浏览浏览、 2:已浏览、  3:约面、4:面试中 5淘汰、6:offer、7:入职 8:未入职 9:未满月)")
   @ApiModelProperty("投放状态(1:未浏览浏览、 2:已浏览、3:已下载  4:约面、5:面试中 6:offer、7:入职 8:未入职 9:未满月 10淘汰)")
    private Integer resumeState;

    @ApiModelProperty(value = "不要传值",required = false)
    Date startTime;
    @ApiModelProperty(value = "不要传值",required = false)
    Date endTime;
}
