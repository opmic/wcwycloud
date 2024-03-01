package com.wcwy.company.query;

import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.NotBlank;

/**
 * ClassName: SendAResumeInformationQuery
 * Description:
 * date: 2023/3/9 14:11
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "子企业投简记录")
public class SendAResumeInformationQuery  extends PageQuery {

    @ApiModelProperty(value = "子企业")
    @NotBlank(message = "子企业不能为空")
    private String company;
    @ApiModelProperty(value = "投放的岗位")
    @NotBlank(message = "岗位id不能为空")
    private String putInPost;
    @ApiModelProperty(value = "投放状态(1:浏览、 2:下载、3排除  4:约面、5:面试中6淘汰、7:offer、8:入职、9:保证期)")
    private Integer resumeState;

    @ApiModelProperty(value = "关键字")
    private String keyword;
}
