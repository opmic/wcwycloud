package com.wcwy.company.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: InterviewerVO
 * Description:
 * date: 2022/12/23 15:03
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "面试官")
public class InterviewerVO {

    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "手机号")
    private String phone;
}
