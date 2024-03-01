package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 面试邀请表
 * @TableName interview_resume
 */

@Data
@ApiModel("修改面试邀请是否同意实体类")
public class AuditInterviewResume {
    /**
     * 面试id
     */

    @ApiModelProperty("面试id")
    @NotBlank(message = "请填写面试id")
    private String interviewId;

    /**
     * 简历投递id
     */
    @ApiModelProperty("简历投递id")
    @NotBlank(message = "请填写简历投递id")
    private String putInResumeId;

    /**
     * 修改状态(1:处理中 2:同意修改 3:不同意修改)
     */
    @ApiModelProperty("修改状态(1:处理中 2:同意修改 3:不同意修改)")
    @NotNull(message = "请填写修改状态")
    private Integer auditState;

    /**
     * 不同意修改原因
     */
    @ApiModelProperty("不同意修改原因")
    private String auditCause;



}