package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业合同
 * @TableName t_company_contract
 */
@TableName(value ="t_company_contract",autoResultMap = true)
@Data
@ApiModel("企业合同")
public class TCompanyContract implements Serializable {
    /**
     * 合同id
     */
    @TableId(value = "contract_id")
    @ApiModelProperty("合同id")
    private String contractId;

    /**
     * 签约合同
     */
    @TableField(value = "sign_contract", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("签约合同")
    private List<String> signContract;

    @TableField(value = "name")
    @ApiModelProperty("服务产品")
    private String name;

    /**
     * 合同有效期
     */
    @TableField(value = "contract_date")
    @ApiModelProperty("合同有效期")
    private LocalDate contractDate;

    /**
     * 审核状态(0:审核中 1:审核成功 2:审核失败)
     */
    @TableField(value = "audit_contract")
    @ApiModelProperty("审核状态(0:审核中 1:审核成功 2:审核失败)")
    private Integer auditContract;

    /**
     * 申请状态(0:有效 1:已过期)
     */
    @TableField(value = "state")
    @ApiModelProperty("申请状态(0:有效 1:已过期)")
    private Integer state;
    /**
     * 审核通过时间
     */
    @TableField(value = "audit_time")
    @ApiModelProperty("审核通过时间")
    private LocalDateTime auditTime;

    /**
     * 失败原因
     */
    @TableField(value = "audit_cause")
    @ApiModelProperty("失败原因")
    private String auditCause;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_id")
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @ApiModelProperty("逻辑删除")
    @TableLogic
    private Integer deleted;



}