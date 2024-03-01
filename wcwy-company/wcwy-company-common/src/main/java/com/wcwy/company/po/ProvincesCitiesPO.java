package com.wcwy.company.po;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: ProvincesCities
 * Description:省市类
 * date: 2022/9/3 8:53
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "省市")
public class ProvincesCitiesPO {
    /**
     * 省份编码
     */
    @TableField(value = "provinceid")
    @ApiModelProperty(value = "省份编码")
    private double provinceid;

    /**
     * 省份
     */
    @TableField(value = "province")
    @ApiModelProperty(value = "省份")
    @NotBlank(message = "省份不能为空")
    private String province;

    /**
     * 城市编码
     */
    @TableField(value = "cityid")
    @ApiModelProperty(value = "城市编码")
    private double cityid;

    /**
     * 城市名称
     */
    @TableField(value = "city")
    @ApiModelProperty(value = "城市编码")
    @NotBlank(message = "城市编码不能为空")
    private String city;


}
