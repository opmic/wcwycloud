package com.wcwy.company.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ClassName: TRecommendShare
 * Description:
 * date: 2023/4/10 20:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "推荐官分享表")
public class TRecommendShare {
    /**
     * id
     */

    @ApiModelProperty("推荐官id")
    private String id;

    /**
     * 姓名
     */

    @ApiModelProperty("姓名")
    private String username;


    /**
     * 头像地址
     */

    @ApiModelProperty("头像地址")
    private String headPath;

    @ApiModelProperty(value = "引入来源(0:用户注册推广 1:职位推广)")
    private Integer origin;

    /**
     * 性别:(1:男 2:女生)
     */
    @ApiModelProperty("性别:(1:男 2:女生)")
    private Integer sex;


    @ApiModelProperty(value = "擅长行业")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,property = "mytype", include=JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes({ //设置对应子类的识别码值
            @JsonSubTypes.Type(value = LinkedHashMap.class, name = "TIndustryAndTypePO"),
            @JsonSubTypes.Type(value = TIndustryAndTypePO.class, name = "TIndustryAndTypePO")
    })
    private List<TIndustryAndTypePO> industry;
    @ApiModelProperty("注册时间")
    private LocalDateTime registrantTime;


    @ApiModelProperty(value = "下载次数")
    private Long download;

    @ApiModelProperty(value = "下载总费用")
    private BigDecimal costs=new BigDecimal(0);
    @ApiModelProperty(value = "收益")
    private BigDecimal earnings=new BigDecimal(0);

    @ApiModelProperty(value = "登录时间")
    private Object loginTime;
    @ApiModelProperty(value = "登录时长")
    private long loginTimeSum;

}
