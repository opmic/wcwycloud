package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 修改邀请入职记录表
 * @TableName update_entry
 */
@Data
@ApiModel("管理员修改入职时间")
public class AdminUpdateEntryVO {

    @ApiModelProperty("投简id")
    @NotBlank(message = "请传入投简id")
    private String putInResumeId;
    /**
     * 修改入职id
     */
    @ApiModelProperty("修改入职id")
    @NotBlank(message = "修改入职id")
    private String updateEntryId;

    @ApiModelProperty("管理员审核状态(0:处理中 1:同意 2:不同意)")
    @NotNull(message = "请填入审核状态")
    private Integer adminAudit;
    /**
     * 不同意原因
     */
    @ApiModelProperty("不同意原因")
    private String adminAuditCause;


}