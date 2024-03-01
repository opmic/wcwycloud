package com.wcwy.post.query;

import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * ClassName: OrderReceivingPostQuery
 * Description:
 * date: 2023/8/14 10:08
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("我的接单岗位查询")
public class OrderReceivingPostQuery extends PageQuery {
    @ApiModelProperty("企业名称")
    private  String company;

    @ApiModelProperty("岗位名称")
    private  String postName;

    @ApiModelProperty("岗位类型(1:入职付 2满月付 3:到面付岗)")
    @Min(value = 1,message = "岗位类型不正确！")
    @Max(value = 3, message = "岗位类型不正确！")
    private  Integer postType;

    @ApiModelProperty("关键字")
    private  String keyword;


    private  String userId;
}
