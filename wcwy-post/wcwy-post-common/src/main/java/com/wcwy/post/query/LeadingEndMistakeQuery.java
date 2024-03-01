package com.wcwy.post.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName: LeadingEndMistakeQuery
 * Description:
 * date: 2023/5/22 13:39
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "查看前端错误")
public class LeadingEndMistakeQuery extends PageQuery {
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间(yyyy-MM-dd)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;
    /**
     * 路由
     */
    @ApiModelProperty("路由")
    private String route;
}
