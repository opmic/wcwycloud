package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: ReportVO
 * Description:
 * date: 2023/9/20 15:22
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "添加举报")
public class ReportPostVO {

    /**
     * 举报原因
     */
    @ApiModelProperty("举报原因")
    @NotBlank(message = "举报原因不能为空！")
    private String cause;

    /**
     * 证据上传
     */
    @ApiModelProperty("证据上传")
    @NotNull(message = "请上传证据！")
    @Size(max = 6,min = 1,message = "证据上传1-6张图片")
    private List<String> evidence;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系方式")
    @NotBlank(message = "联系方式不能为空！")
    private String phone;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    @NotBlank(message = "企业名称不能为空！")
    private String companyName;

    /**
     * 职位名称
     */
    @ApiModelProperty("职位名称")
    @NotBlank(message = "职位名称不能为空！")
    private String postName;
    /**
     * 补充说明
     */
    @ApiModelProperty("补充说明")
    @NotBlank(message = "补充说明不能为空！")
    private String replenish;
    /**
     * 职位id
     */

    @ApiModelProperty("职位id")
    @NotBlank(message = "职位id不能为空！")
    private String postId;





    /**
     * 求职者id
     */
/*    @ApiModelProperty("求职者id")
    private String jobHunterId;

    *//**
     * 求职者名称
     *//*
    @ApiModelProperty("求职者名称")
    private String jobHunterName;*/

    /**
     * 类型(0:岗位1:求职者)
     */
/*    @ApiModelProperty("类型(0:岗位1:简历)")
    @NotNull(message = "类型不能为空！")
    private Integer type;*/



}
