package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * ClassName: StationVO
 * Description:
 * date: 2022/9/6 14:20
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "岗位表")
public class StationVO {
    /**
     * 职位标签
     */
    @ApiModelProperty(value = "职位标签")
    private String positionLabel;

    /**
     * 行业类型id
     */
    @ApiModelProperty(value = "行业类型id")
    private Integer industryTypeId;

}
