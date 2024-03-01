package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 面试邀请表
 * @TableName interview_resume
 */
@Data
@ApiModel("接受面试邀请表")
public class SuggestInterviewResume {
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
    @NotBlank(message = "简历投递id")
    private String putInResumeId;



    /**
     * 是否接受面试(1:未接受 2:已接受 3:不接受)
     */

    @ApiModelProperty("是否接受面试(1:处理中 2:已接受 3:不接受)")
    @NotNull(message = "是否接受面试(1:处理中 2:已接受 3:不接受)")
    private Integer take;

    /**
     * 建议修改时间
     */

    @ApiModelProperty("建议修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "时间不能小于今天")
    private LocalDateTime suggestTime;

    /**
     * 不接受原因
     */
    @ApiModelProperty("不接受原因")
    private String noTakeCause;

}