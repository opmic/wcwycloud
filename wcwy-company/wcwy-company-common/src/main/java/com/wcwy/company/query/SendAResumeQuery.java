package com.wcwy.company.query;

import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * ClassName: SendAResume
 * Description:
 * date: 2022/12/29 9:07
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "查看未投递的求职者")
public class SendAResumeQuery extends PageQuery {
    @ApiModelProperty("岗位id")
    @NotBlank(message = "岗位id不能为空!")
    public String postId;

    @ApiModelProperty("关键字搜索")
    public String search;

    @ApiModelProperty("注:不需要填")
    public List<String> list;

    @ApiModelProperty("注:不需要填")
    public String userId;

}
