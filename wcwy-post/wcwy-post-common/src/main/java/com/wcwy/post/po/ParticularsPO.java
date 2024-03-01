package com.wcwy.post.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClassName: ParticularsPO
 * Description:订单详情表
 * date: 2023/7/5 16:26
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("订单详情副属表")
public class ParticularsPO {
    /**
     * 用户ID
     */
    @ApiModelProperty(value ="求职者id")
    private String jobHunter;
    /**
     * 用户姓名
     */
    @ApiModelProperty(value ="头像" )
    private String avatar;
    @ApiModelProperty(value ="求职者姓名" )
    private String userName;
    @ApiModelProperty(value ="参加工作时间" )
    private LocalDate workTime;
    @ApiModelProperty(value ="学历" )
    private String education;
    @ApiModelProperty(value ="生日" )
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;


    @ApiModelProperty(value = " 岗位发布类型(0普通岗位 1:赏金岗位 2猎头岗位 3到面付岗位 4:简历付校 5:简历付职)")
    private Integer postType;
    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;

    /**
     * 岗位开始薪资
     */
    @ApiModelProperty(value = "岗位开始薪资")
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */

    @ApiModelProperty(value = "岗位结束薪资")
    private BigDecimal endSalary;
    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty("面试时间")
    private LocalDateTime interviewTime;
    @ApiModelProperty("入职时间")
    private LocalDateTime entryTime;

    @ApiModelProperty(value = "目前年薪")
    private BigDecimal currentSalary;
}
