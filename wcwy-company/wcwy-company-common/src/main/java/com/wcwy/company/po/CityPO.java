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
@ApiModel(value = "省市实体类")
public class CityPO {
    @ApiModelProperty(value = "省")
    @NotBlank(message = "省不为空")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    @NotBlank(message = "市不为空")
    private String city;

    @ApiModelProperty(value = "详细地址")
   /* @NotBlank(message = "详细地址不为空")*/
    private String address;
}
