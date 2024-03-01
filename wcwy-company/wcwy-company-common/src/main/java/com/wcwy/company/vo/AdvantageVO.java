package com.wcwy.company.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * ClassName: AdvantageVO
 * Description:
 * date: 2023/4/15 11:04
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("添加个人优势")
public class AdvantageVO {

    @ApiModelProperty("个人优势")
    private String advantage;

    @ApiModelProperty("简历id")
    private String resumeId;
}
