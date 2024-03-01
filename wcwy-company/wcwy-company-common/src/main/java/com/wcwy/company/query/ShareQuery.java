package com.wcwy.company.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * ClassName: ShareQuery
 * Description:
 * date: 2023/4/10 15:04
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("分享查询")
public class ShareQuery extends PageQuery {
    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @ApiModelProperty("关键词")
    private  String keyword;

    @ApiModelProperty(value = "引入来源(0:用户注册推广 1:职位推广)")
    private Integer origin;
}
