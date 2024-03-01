package com.wcwy.post.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: AddressPO
 * Description:
 * date: 2022/12/1 8:48
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "邮寄详细地址")
public class AddressPO {
    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    @NotBlank(message = "城市名称不能为空")
    private String province;
    /**
     * 城市名称
     */
    @ApiModelProperty(value = "城市名称")
    @NotBlank(message = "城市名称不能为空")
    private String city;

    @ApiModelProperty(value = "详细地址")
    @NotBlank(message = "详细地址不能为空")
    private String address;

}
