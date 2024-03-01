package com.wcwy.company.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ClassName: TCompanyShare
 * Description:
 * date: 2023/4/10 20:14
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "查看邀请企业")
public class TCompanySharePO {
    /**
     * 企业Id
     */
    @ApiModelProperty(value = "企业Id")
    private String companyId;

    @ApiModelProperty(value = "公司类型id")
    private String companyTypeId;
    /**
     * 头像路径
     */
    @ApiModelProperty(value = "头像路径")
    private String avatar;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String companyName;

    /**
     * 企业规模
     */
    @TableField(value = "firm_size")
    @ApiModelProperty(value = "企业规模")
    private String firmSize;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;


    @ApiModelProperty(value = "下载次数")
    private Long download;

    @ApiModelProperty(value = "下载总费用")
    private BigDecimal costs=new BigDecimal(0);
    @ApiModelProperty(value = "收益")
    private BigDecimal earnings=new BigDecimal(0);
    @ApiModelProperty(value = "引入来源(0:用户注册推广 1:职位推广)")
    private Integer origin;
    @ApiModelProperty(value = "登录时间")
    private Object loginTime;

    @ApiModelProperty(value = "登录时长")
    private long loginTimeSum;
}
