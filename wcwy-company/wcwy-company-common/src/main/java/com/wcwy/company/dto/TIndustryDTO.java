package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * ClassName: TIndustryDot
 * Description:
 * date: 2022/9/2 15:04
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "行业信息表")
public class TIndustryDTO {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Integer id;

    /**
     * 行业编码
     */
    @ApiModelProperty(value = "行业编码")
    private String code;

    /**
     * 行业名称
     */
    @ApiModelProperty(value = "行业名称")
    private String name;

    @ApiModelProperty(value = "行业类型表")
    private List<TIndustryTypeDTO> children;



}
