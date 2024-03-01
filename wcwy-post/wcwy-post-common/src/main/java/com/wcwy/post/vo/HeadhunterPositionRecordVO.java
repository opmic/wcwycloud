package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 猎头岗位金额记录表
 * @TableName headhunter_position_record
 */
@Data
@ApiModel("猎头岗位金额记录表")
public class HeadhunterPositionRecordVO  {


    /**
     * 月份
     */
    @ApiModelProperty(value = "月份")
    private Integer month;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal money;

    /**
     * 绑定的岗位
     */
  /*  @ApiModelProperty(value = "绑定的岗位")
    private String postId;*/

}