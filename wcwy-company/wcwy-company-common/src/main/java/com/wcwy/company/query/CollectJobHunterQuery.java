package com.wcwy.company.query;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * ClassName: CollectJobHunterQuery
 * Description:
 * date: 2023/4/3 14:23
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@TableName(value ="collerct_post")
@Data
@ApiModel("查询收藏的简历")
public class CollectJobHunterQuery extends PageQuery {
    @ApiModelProperty("关键词")
    public String keyword;
    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate startTime;
    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate endTime;
}
