package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.po.ProvincesCitiesPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ClassName: OneInterviewDTO
 * Description:
 * date: 2023/6/7 14:10
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "确认面试获取一次面试")
public class OneInterviewDTO {
    /**
     * 面试时间
     */
    @ApiModelProperty(value = "面试时间")
    private LocalDateTime interviewTime;

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

    /**
     * 所在城市
     */
    @ApiModelProperty(value = "所在城市")
    private ProvincesCitiesPO workCity;
    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;
    @ApiModelProperty(value ="用户姓名" )
    private String userName;
    @ApiModelProperty("确认面试(0:未操作 1:确认面试)")
    private Integer affirmInterview;
}
