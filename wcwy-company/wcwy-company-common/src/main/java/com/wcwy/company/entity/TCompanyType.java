package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 公司类型
 * @TableName t_company_type
 */
@TableName(value ="t_company_type")
@Data
@ApiModel(value ="公司类型" )
public class TCompanyType implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "typeid", type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer typeid;

    /**
     * 公司类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "公司类型")
    private String type;

}