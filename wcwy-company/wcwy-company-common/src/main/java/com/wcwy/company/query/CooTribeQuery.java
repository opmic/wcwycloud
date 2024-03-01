package com.wcwy.company.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

/**
 * ClassName: CooTribeQuery
 * Description:
 * date: 2024/1/19 13:39
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("帖子查询")
public class CooTribeQuery extends PageQuery {
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("推荐官id")
    private String userId;
    @ApiModelProperty("发帖id")
    private Long id;
    @ApiModelProperty("类型(0:文章 1:问答 2:心得 3回答)")
    private Integer type;

    private String loginUserId;

    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endTime;
}
