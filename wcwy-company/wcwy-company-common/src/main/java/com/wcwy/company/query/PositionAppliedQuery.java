package com.wcwy.company.query;

import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: PositionAppliedQuery
 * Description:
 * date: 2023/8/30 16:08
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("求职者意向职位条件查询")
public class PositionAppliedQuery extends PageQuery {
    @ApiModelProperty(value = "关键字")
    private String keyword;
    @ApiModelProperty(value = "来源(0自主注册 1:他人引入)")
    private Integer source=0;

    private String userid;
}
