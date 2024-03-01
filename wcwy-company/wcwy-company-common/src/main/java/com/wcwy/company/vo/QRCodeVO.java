package com.wcwy.company.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * ClassName: QRCodeVO
 * Description:
 * date: 2023/5/20 9:25
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("二维码接收")
public class QRCodeVO {
    @NotBlank(message = "跳转路径不能为空！")
    @ApiModelProperty("路径")
    private String path;
    @NotNull(message = "宽不能为空！")
    @ApiModelProperty("宽")
    private int qrWidth;
    @ApiModelProperty("高")
    @NotNull(message = "高不能为空！")
    private Integer qrHeight;
    @ApiModelProperty("身份(0:企业 1:推荐官 2:求职者 3:猎企 4:校园推荐官)")
    @Max(value = 4 ,message = "身份选择不正确！")
    @Min(value = 0 ,message = "身份选择不正确！")
    private Integer type;
}
