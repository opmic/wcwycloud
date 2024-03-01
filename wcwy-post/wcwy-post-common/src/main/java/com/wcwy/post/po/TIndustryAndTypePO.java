package com.wcwy.post.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: TIndustryAndTypePO
 * Description:
 * date: 2022/9/3 8:59
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel(value = "行业信息及行业类型表")
@Data
public class TIndustryAndTypePO {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "行业信息主键ID")
    private Integer id;

    /**
     * 行业编码
     */
    @ApiModelProperty(value = "行业编码")
    private String code;

    /**
     * 行业名称
     */

    @ApiModelProperty(value = "行业名称")
    private String name;

    /**
     * 主键ID
     */

    @ApiModelProperty(value ="行业类型主键ID" )
    private Integer typeId;

    /**
     * 标签编码
     */

    @ApiModelProperty(value ="标签编码" )
    private String labelId;

    /**
     * 标签名称
     */
    @ApiModelProperty(value ="标签名称" )
    private String labelName;

}
