package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 投递简历表
 *
 * @TableName put_in_resume
 */

@Data
@ApiModel("求职者投递简历表")
public class PutInResumeVO implements Serializable {
    /**
     * 投放的岗位
     */
    @ApiModelProperty("投放的岗位")
    @NotBlank(message = "岗位id不能为空")
    private String putInPost;

    /**
     * 投放的公司
     */
    @ApiModelProperty("投放的公司")
    @NotBlank(message = "投放的公司id不能为空")
    private String putInComppany;

    @ApiModelProperty("投简说明")
    private String explains;
    /**
     * 求职者
     */
    @ApiModelProperty("求职者")
    @NotBlank(message = "求职者id不能为空")
    private String putInJobhunter;

    @ApiModelProperty("推广时间")
    private Long time;

    @ApiModelProperty("邀请链接")
    private  String qrcode;

}