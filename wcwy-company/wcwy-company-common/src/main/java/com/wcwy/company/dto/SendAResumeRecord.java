package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ClassName: SendAResumeRecord
 * Description:
 * date: 2023/3/9 8:43
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "子账号投简数据")
public class SendAResumeRecord {
    /**
     * 企业Id
     */
    @ApiModelProperty(value = "企业Id")
    private String companyId;



    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    /**
     * 头像路径
     */
    @ApiModelProperty(value = "头像路径")
    private String avatar;


    @ApiModelProperty(value = "企业LOGO")
    private String logoPath;
    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String companyName;


    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别(0:未知 1:男 2:女生)")
    private Integer sex;
    /**
     * 职务
     */

    @ApiModelProperty(value = "职务")
    private String jobTitle;

    /**
     * 联系方式
     */

    @ApiModelProperty(value = "联系方式")
    private String contactPhone;



    @ApiModelProperty(value = "求职者投简")
    private  Integer jobSeeker;

    @ApiModelProperty(value = "求推荐官投简")
    private  Integer recommend;
}
