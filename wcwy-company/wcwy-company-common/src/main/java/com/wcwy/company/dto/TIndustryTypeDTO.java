package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wcwy.company.entity.Station;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * ClassName: TIndustryTypeDTO
 * Description:
 * date: 2022/9/2 15:48
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@TableName(value ="t_industry_type")
@ApiModel(value = "行业类型表")
@Data
public class TIndustryTypeDTO {

    /**
     * 标签编码
     */
    @TableField(value = "label_id")
    @ApiModelProperty(value ="标签编码" )
    private String code;

    /**
     * 标签名称
     */
    @TableField(value = "label_name")
    @ApiModelProperty(value ="标签名称" )
    private String name;


    @ApiModelProperty(value ="岗位表" )
    List<StationDTO> children;
}
