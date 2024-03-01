package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 求职者附件
 * @TableName t_job_hunter_attachment
 */
@TableName(value ="t_job_hunter_attachment")
@Data
@ApiModel(value = "求职者附件")
public class TJobHunterAttachment implements Serializable {
    /**
     * 附件id
     */
    @TableId(value = "attachment_id")
    @ApiModelProperty(value = "附件id")
    private String attachmentId;

    /**
     * 求职者id
     */
    @TableField(value = "t_job_hunter_id")
    @ApiModelProperty(value = "求职者id")
    private String tJobHunterId;

    /**
     * 附件地址
     */
    @TableField(value = "path")
    @ApiModelProperty(value = "附件地址")
    private String path;

    /**
     * 上传时间
     */
    @TableField(value = "create_date")
    @ApiModelProperty(value = "上传时间")
    private LocalDate createDate;

    /**
     * 是否置顶(0:置顶 1:不置顶)
     */
    @TableField(value = "top")
    @ApiModelProperty(value = "是否置顶(0:置顶 1:不置顶)")
    private Integer top;




}