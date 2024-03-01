package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 发送offer表
 * @TableName invite_entry
 */
@Data
@ApiModel("求职者投简发送offer表")
public class JobHunterInviteEntryVO {

    /**
     * offer路径
     */
    @ApiModelProperty("offer路径")
    @NotBlank(message = "请删除offer文件")
    private String offerPath;

    /**
     * 入职时间
     */
    @ApiModelProperty("入职时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Future(message = "入职时间不能小于当前")
    @NotNull(message ="入职时间不能为空")
    private LocalDate entryTime;

    /**
     * 入职岗位id
     */
    @ApiModelProperty("入职岗位id")
    @NotBlank(message = "入职岗位id不能为空!")
    private String postId;

    /**
     * 投简id
     */
    @ApiModelProperty("投简id")
    @NotBlank(message = "投简id不能为空!")
    private String putInResumeId;



    /**
     * 入职年薪薪水
     */
    @ApiModelProperty("入职年薪薪水")
    @NotNull(message = "入职年薪薪水不能为空")
    private BigDecimal salary;




}