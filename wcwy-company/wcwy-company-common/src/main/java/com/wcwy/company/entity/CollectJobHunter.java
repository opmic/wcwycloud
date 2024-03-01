package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业收藏表
 * @TableName collect_job_hunter
 */
@TableName(value ="collect_job_hunter")
@Data
@ApiModel(value = "企业收藏表")
public class CollectJobHunter implements Serializable {
    /**
     * 收藏
     */
    @TableId(value = "collect_id")
    @ApiModelProperty("收藏id")
    private String collectId;

    /**
     * 企业
     */
    @TableField(value = "t_company_id")
    @ApiModelProperty("企业")
    private String tCompanyId;

    /**
     * 求职者id
     */
    @TableField(value = "t_job_hunter_id")
    @ApiModelProperty("求职者id")
    private String tJobHunterId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}