package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: LeadingEndMistakeVO
 * Description:
 * date: 2023/5/20 11:47
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("前端错误")
public class LeadingEndMistakeVO {
    /**
     * 错误
     */
    @ApiModelProperty("错误")
    private List<Object> mistake;

    /**
     * 路由
     */
    @ApiModelProperty("路由")
    private String route;

    /**
     * 路由名
     */
    @ApiModelProperty("路由名")
    private String routeName;

    /**
     * 访问地址
     */

    @ApiModelProperty("访问地址")
    private String visitPath;

}
