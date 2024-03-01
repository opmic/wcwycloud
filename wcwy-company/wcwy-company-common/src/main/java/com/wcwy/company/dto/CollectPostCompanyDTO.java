package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.po.ProvincesCitiesPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 岗位收藏表
 * @TableName collerct_post
 */
@Data
@ApiModel("岗位收藏+企业信息")
public class CollectPostCompanyDTO implements Serializable {
    /**
     * 岗位收藏id
     */
    @ApiModelProperty("岗位收藏id")
    private String collerctPostId;
    @ApiModelProperty(value = "企业Id")
    private String companyId;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "企业名称")
    private String companyName;
    /**
     * 岗位id
     */
    @ApiModelProperty(value = "岗位id")
    private String postId;



    /**
     * 岗位开始薪资
     */
    @ApiModelProperty(value = "岗位开始薪资")
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */
    @ApiModelProperty(value = "岗位结束薪资")
    private BigDecimal endSalary;

    /**
     * 所在城市
     */

    @ApiModelProperty(value = "所在城市")
    private ProvincesCitiesPO workCity;
    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;
    /**
     * 岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付)
     */
    @ApiModelProperty(value = "岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付)")
    private Integer postType;
    @ApiModelProperty(value = "身份类型0:企业发布 1:推荐官发布")
    private Integer type;

    @ApiModelProperty(value = "企业类型0:普通企业 1:猎企业")
    private Integer companyType=1;
}