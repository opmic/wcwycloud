package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 修改邀请入职记录表
 * @TableName update_entry
 */
@Data
@ApiModel("修改入职时间")
public class UpdateEntryVO implements Serializable {



    @ApiModelProperty("修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Future(message = "时间不能小于今天")
    private LocalDate updateEntryTime;

    /**
     * 邀请offerid
     */
    @ApiModelProperty("邀请offerid")
    @NotBlank(message = "邀请offerid不能为空")
    private String inviteEntryId;

    /**
     * 修改原因
     */
    @ApiModelProperty("修改原因")
    @NotBlank(message = "修改原因不能为空")
    private String updateCause;

    /**
     * 投简id
     */
    @ApiModelProperty("投简id")
    @NotBlank(message = "请传入投简id")
    private String putInResumeId;
}