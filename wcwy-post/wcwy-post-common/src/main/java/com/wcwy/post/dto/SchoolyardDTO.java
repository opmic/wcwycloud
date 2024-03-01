package com.wcwy.post.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ClassName: SchoolyardDTO
 * Description:
 * date: 2023/10/12 13:31
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "校园招聘职位")
public class SchoolyardDTO {
    /**
     * 企业Id
     */
    @ApiModelProperty(value = "企业Id")
    private String companyId;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String companyName;

    /**
     * 公司类型id
     */
    @ApiModelProperty(value = "公司类型id")
    private String companyTypeId;

    /**
     * 企业LOGO
     */
    @ApiModelProperty(value = "企业LOGO")
    private String logoPath;
    /**
     * 企业规模
     */
    @ApiModelProperty(value = "企业规模")
    private String firmSize;

    /**
     * 行业类型
     */
    @ApiModelProperty(value = "行业类型")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "mytype", include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes({ //设置对应子类的识别码值
            @JsonSubTypes.Type(value = LinkedHashMap.class, name = "TIndustryAndTypePO"),
            @JsonSubTypes.Type(value = TIndustryAndTypePO.class, name = "TIndustryAndTypePO")
    })
    private List<TIndustryAndTypePO> industry;

    @ApiModelProperty(value = "岗位名称")
    private List<Map> postName;

    @ApiModelProperty(value = "职位福利")
    private Set<String> postWealId;


}
