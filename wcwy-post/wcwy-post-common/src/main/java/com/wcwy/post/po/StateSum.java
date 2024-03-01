package com.wcwy.post.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * ClassName: StateSum
 * Description:
 * date: 2022/12/28 15:06
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel("获取岗位各状态数量")
@Data
public class StateSum {
    @ApiModelProperty("在线中")
    private Integer online;
    @ApiModelProperty("未上线")
    private Integer notOnline;
    @ApiModelProperty("审核中")
    private Integer inReview;
    @ApiModelProperty("审核未通过")
    private Integer notPass;
}
