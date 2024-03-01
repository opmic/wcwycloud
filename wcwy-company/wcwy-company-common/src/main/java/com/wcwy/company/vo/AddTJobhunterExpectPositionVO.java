package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 期望职位表
 * @TableName t_jobhunter_expect_position
 */
@Data
@ApiModel(value = "导入1添加期望职位")
public class AddTJobhunterExpectPositionVO {
    /**
     * 期望职位
     */
    @ApiModelProperty(value = "期望职位")
    @NotEmpty(message = "期望职位不能为空")
    private List<String> positionName;
    /**
     * 期望行业
     */
    @ApiModelProperty(value = "期望行业")
    @NotEmpty(message = "期望职位不能为空")
    private List<String> desiredIndustry;
    /**
     * 工作城市
     */
    @ApiModelProperty(value = "工作城市")
    @NotEmpty(message = "期望职位不能为空")
    private List<String> workCity;

    /**
     * 期望年薪
     */
    @TableField(value = "expect_salary")
    @ApiModelProperty(value = "期望年薪(单位:W)")
    @NotNull(message = "期望年薪不能为空")
    private BigDecimal expectSalary;


}