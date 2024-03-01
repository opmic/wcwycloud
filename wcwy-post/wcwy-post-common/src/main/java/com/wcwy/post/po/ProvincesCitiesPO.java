package com.wcwy.post.po;

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
    @ApiModelProperty(value = "省份编码")
    private double provinceid;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String province;

    /**
     * 城市编码
     */
    @ApiModelProperty(value = "城市编码")
    private double cityid;

    /**
     * 城市名称
     */
    @ApiModelProperty(value = "城市名称")
    @NotBlank(message = "城市名称不能为空")
    private String city;



}
