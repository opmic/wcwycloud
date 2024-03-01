package com.wcwy.post.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * ClassName: OrderReceivingQuery
 * Description:
 * date: 2023/5/25 11:10
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("收藏岗位查询")
public class OrderReceivingCollectQuery extends PageQuery {
    @ApiModelProperty(value = " 岗位发布类型(0普通岗位 1:赏金岗位 2猎头岗位 3到面付岗位,4简历付·校,5简历付·职)")
    private Integer postType;
    @ApiModelProperty(value = "关键字")
    private String keyword;
    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate beginTime;


    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endTime;
}
