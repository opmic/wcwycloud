package com.wcwy.company.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 修改邀请入职记录表
 * @TableName update_entry
 */

@Data
@ApiModel("修改邀请入职记录")
public class UpdateEntryPO  {
    /**
     * 修改入职id
     */
    @ApiModelProperty("修改入职id")
    private String updateEntryId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDate updateEntryTime;

    /**
     * 邀请offerid
     */
    @ApiModelProperty("邀请offerid")
    private String inviteEntryId;

    /**
     * 修改原因
     */
    @ApiModelProperty("修改原因")
    private String updateCause;

    /**
     * 管理员审核状态(0:处理中 1:同意 2:不同意)
     */
    @ApiModelProperty("管理员审核状态(0:处理中 1:同意 2:不同意)")
    private Integer adminAudit;

    /**
     * 管理员处理时间
     */
    @ApiModelProperty("管理员处理时间")
    private LocalDateTime adminTime;

    /**
     * 不同意原因
     */
    @ApiModelProperty("不同意原因")
    private String adminAuditCause;

    /**
     * 处理人
     */
    @ApiModelProperty("处理人")
    private String adminId;

    /**
     * 投简人id
     */
    @ApiModelProperty("投简人id")
    private String putId;

    /**
     * 投简人审核状态(0:处理中 1:同意 2:不同意)
     */
    @ApiModelProperty("投简人审核状态(0:处理中 1:同意 2:不同意)")
    private Integer putAudit;

    /**
     * 投简人处理时间
     */
    @ApiModelProperty("投简人处理时间")
    private LocalDateTime putTime;

    /**
     * 投简人不同意原因
     */
    @ApiModelProperty("投简人不同意原因")
    private String putAuditCause;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

}