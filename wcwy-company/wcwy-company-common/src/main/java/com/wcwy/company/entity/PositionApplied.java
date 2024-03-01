package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 求职者意向职位
 * @TableName position_applied
 */
@TableName(value ="position_applied")
@Data
@ApiModel(value = "求职者意向职位")
public class PositionApplied implements Serializable {
    /**
     * 意向id
     */
    @TableId(value = "position_applied_id")
    @ApiModelProperty(value = "意向id")
    private String positionAppliedId;

    /**
     * 来源(0自主注册 1:他人引入)
     */
    @TableField(value = "source")
    @ApiModelProperty(value = "来源(0自主注册 1:他人引入)")
    private Integer source;
    @TableField(value = "post_type")
    @ApiModelProperty(value = "岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付)")
    private Integer postType;
    /**
     * 意向求职者
     */
    @TableField(value = "job_hunter")
    @ApiModelProperty(value = "意向求职者")
    private String jobHunter;

    /**
     * 意向岗位
     */
    @TableField(value = "post_label")
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;

    /**
     * 推广时间
     */
    @TableField(value = "promotion_time")
    @ApiModelProperty(value = "推广时间")
    private LocalDateTime promotionTime;

    /**
     * 投简时间
     */
    @TableField(value = "put_time")
    @ApiModelProperty(value = "投简时间")
    private LocalDateTime putTime;
    @TableField(value = "company_id")
    @ApiModelProperty(value = "企业id")
    private String companyId;
    @TableField(value = "recommend_id")
    @ApiModelProperty(value = "推荐官id")
    private String recommendId;

}