package com.wcwy.company.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClassName: TRecommendQuery
 * Description:
 * date: 2022/9/8 11:40
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "推荐官条件查询")
public class TRecommendQuery extends PageQuery {
    /**
     * id
     */
    @ApiModelProperty(value ="id" )
    private String id;
    /**
     * 姓名
     */
    @ApiModelProperty(value ="姓名(模糊查询)" )
    private String username;
    /**
     * 学历
     */
/*    @ApiModelProperty(value ="学历" )
    private String education;*/
    /**
     * 性别:(1:男 2:女生)
     */
/*    @ApiModelProperty(value ="性别:(1:男 2:女生)" )
    private Integer sex;*/
    /**
     * 电话号码
     */
    @ApiModelProperty(value ="电话号码（模糊查询）" )
    private String phone;
    /**
     * 身份证
     */
    @ApiModelProperty(value ="身份证" )
    private String card;



    /**
     * 链接分享人
     */
    @ApiModelProperty(value ="链接分享人" )
    private String sharePerson;


}
