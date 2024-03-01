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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 发送offer表
 * @TableName invite_entry
 */

@Data
@ApiModel(value = "添加offer")
public class InviteEntryVO {


    /**
     * offer路径
     */
    @ApiModelProperty(value = "offer路径")
    @NotBlank(message = "请上传offer")
    private String offerPath;

    /**
     * 入职日期
     */
    @ApiModelProperty(value = "入职日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Future(message = "入职时间不能小于今天!")
    @NotNull(message = "入职时间不能为空!")
    private LocalDate entryDate;

    /**
     * 入职时间
     */
    @ApiModelProperty(value = "入职时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @NotNull(message = "入职时间不能为空!")
    private LocalTime entryTime;

    @ApiModelProperty(value = "录用人")
    @NotBlank(message = "录用人不能为空!")
    private String appointee;

    /**
     * 投简id
     */
    @ApiModelProperty(value = "投简id")
    @NotBlank(message = "投简id不能为空")
    private String putInResumeId;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    @NotBlank(message = "岗位名称不能为空")
    private String postName;


    /**
     * 对接人姓名
     */
    @ApiModelProperty(value = "对接人姓名")
    @NotBlank(message = "入职对接人不能为空")
    private String receivedBy;

    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码")
    @NotBlank(message = "电话号码不能为空")
    private String receivedByPhone;

    /**
     * 备注
     */
    @ApiModelProperty(value = "携带资料")
    @NotBlank(message = "请备注好需要携带的资料！")
    private String remark;
    @ApiModelProperty(value = "税前年薪")
    @NotNull(message = "税前年薪不能为空!")
    private BigDecimal hiredBounty;
}