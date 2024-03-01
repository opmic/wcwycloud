package com.wcwy.company.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: DetailedAddressPO
 * Description:
 * date: 2023/3/31 15:15
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "详细地址")
public class DetailedAddressPO {
    @ApiModelProperty(value = "省")
    @NotBlank(message = "省不为空")
    private String province;
    @ApiModelProperty("经度")
    private Double longitude;
    @ApiModelProperty(value = "纬度")
    private Double latitude;
    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "详细地址")
    private String address;
}
