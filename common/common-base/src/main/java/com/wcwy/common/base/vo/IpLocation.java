package com.wcwy.common.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: IpLocation
 * Description:
 * date: 2023/8/7 9:05
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class IpLocation implements Serializable {

    @ApiModelProperty("ip地址")
    private String ip;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("服务商")
    private String isp;
}

