package com.wcwy.company.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 企业查看用户简历权限表
 * @TableName company_user_role
 */
@Data
@ApiModel("查看下载简历筛选")
public class CompanyUserRoleQuery extends PageQuery {

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value ="年龄(1:18-25 2:26-30 3:31-35 4:36-40 5:41-45 6：46以上 )" )
    private  Integer birthday;

    @ApiModelProperty(value = "来源(0:求职者 1:推荐官)")
    private Integer source;

    @ApiModelProperty(value = "学历(2中专/中技 3:高中 4:大专 5:本科 6:硕士 7:博士)")
    private List<String> education;

    @ApiModelProperty(value = "不要传值",required = false)
    Date startTime;
    @ApiModelProperty(value = "不要传值",required = false)
    Date endTime;
}