package com.wcwy.company.query;

import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

/**
 * ClassName: ReferrerRecordQuery
 * Description:
 * date: 2023/5/23 14:50
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("推荐人数据记录查询")
public class ReferrerRecordQuery extends PageQuery {

    @ApiModelProperty(value = "人才来源(0:分享引流 1:人才委托 2新增人才 3应聘简历)")
    private Integer correlationType;
/*    @ApiModelProperty(value = "求职期望")
    private String positionName;*/
    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "用户性别（0男 1女")
    private Integer sex;

    @ApiModelProperty("工作经验(1:一年以内 2:1-3年 3:3-5年 4:5-10年 5:10年以上)")
    private Integer workExperience;
    @ApiModelProperty("学历(传数字)0:不限 1:初中及以下2:中专/中技3:高中4:大专5:本科6:硕士7:博士")
    private Integer education;
    @ApiModelProperty("开始目前薪资")
    private Integer tartAnnualSalary;
    @ApiModelProperty("结束目前薪资")
    private Integer endAnnualSalary;
    @ApiModelProperty("开始期望薪资")
    private Integer tartExpectationAnnualSalary;
    @ApiModelProperty("结束期望薪资")
    private Integer endExpectationAnnualSalary;
    @ApiModelProperty("现在所在城市")
    private String city;
    @ApiModelProperty("期望工作地")
    private String expectationCity;

    private Date beginAge;

    private Date endAge;
    @ApiModelProperty(value ="生日(1:18-25 2:26-30 3:31-35 4:36-40 5:41-45 6：46以上 )" )
    private Integer birthday;
    private Date beginWorkTime;
    private Date endWorkTime;

    @ApiModelProperty("岗位id")
    private String postId;
}
