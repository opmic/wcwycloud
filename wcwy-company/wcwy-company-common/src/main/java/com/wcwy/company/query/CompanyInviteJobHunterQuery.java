package com.wcwy.company.query;

import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * ClassName: CompanyInviteJobHunterQuery
 * Description:
 * date: 2023/11/6 9:07
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("企业查询邀请的求职者")
public class CompanyInviteJobHunterQuery extends PageQuery {

    private String userId;

    @ApiModelProperty(value ="生日(1:18-25 2:26-30 3:31-35 4:36-40 5:41-45 6：46以上 )" )
    private Integer age;
    @ApiModelProperty(value = "不要传值",required = false)
    Date startTime;
    @ApiModelProperty(value = "不要传值",required = false)
    Date endTime;

    @ApiModelProperty(value ="用户性别（0男 1女 2未知）" )
    private Integer sex;

    @ApiModelProperty("活跃度(0:今日活跃 1:本周活跃 2:本月活跃 3:半年活跃 )")
    private Integer ap;
    private LocalDate apBeginDate;
    private LocalDate apEndDate;
    @ApiModelProperty("目前开始年薪")
    private Double beginCurrentAnnualSalary;
    @ApiModelProperty("结束年薪")
    private Double endCurrentAnnualSalary;
    @ApiModelProperty("开始期望年薪")
    private Double  beginExpectedAnnualSalary;
    @ApiModelProperty("结束期望年薪")
    private Double  endExpectedAnnualSalary;

    @ApiModelProperty("学历")
    private Integer educationBackground;

    @ApiModelProperty("当前城市")
    private String currentCity;

    @ApiModelProperty("期望城市")
    private String desiredCity;

    @ApiModelProperty("关键字查询")
    private String keyword;
}
