package com.wcwy.company.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: interviewerPO
 * Description:
 * date: 2023/3/31 15:18
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "面试官")
public class InterviewerPO {

    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "电话")
    private String phone;
}
