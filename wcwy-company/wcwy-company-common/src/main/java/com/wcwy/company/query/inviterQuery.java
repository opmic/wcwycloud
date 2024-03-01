package com.wcwy.company.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClassName: inviterQuery
 * Description:
 * date: 2022/12/5 11:14
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("查看邀请人表")
public class inviterQuery extends PageQuery {
    @ApiModelProperty(value = "邀请的人id")
    private String id;
    @ApiModelProperty(value = "企业名称/求职者姓名")
    private String companyName;

    @ApiModelProperty(value = "电话")
    private String phoneNumber;
    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate beginDate;

    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty("登录id")
    @NotBlank(message = "登录用户不能为空")
    private String loginUser;
}
