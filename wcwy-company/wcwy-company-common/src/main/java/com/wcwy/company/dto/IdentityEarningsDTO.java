package com.wcwy.company.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ClassName: IdentityEarningsDTO
 * Description:
 * date: 2023/8/11 11:43
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("我的收益详情-分享收益")
public class IdentityEarningsDTO {

    @ApiModelProperty(value ="参加工作时间" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate workTime;

    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("性别")
    private Integer sex;

    @ApiModelProperty("年龄")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

  /*  @ApiModelProperty("岗位")
    private String postName;*/

    @ApiModelProperty("企业名称")
    private String companyName;

/*    @ApiModelProperty("开始薪资")
    private BigDecimal endSalary;

    @ApiModelProperty("结束薪资")
    private BigDecimal beginSalary;*/


    @ApiModelProperty("下载时间")
    private LocalDateTime downloadTime;


    @ApiModelProperty("总费用")
    private BigDecimal aggregateAmount;

    @ApiModelProperty("我的收益")
    private BigDecimal earnings;

    @ApiModelProperty(value = "擅长行业")
    @JsonSubTypes({ //设置对应子类的识别码值
            @JsonSubTypes.Type(value = LinkedHashMap.class, name = "TIndustryAndTypePO"),
            @JsonSubTypes.Type(value = TIndustryAndTypePO.class, name = "TIndustryAndTypePO")
    })
    private List<TIndustryAndTypePO> industry;

    @ApiModelProperty(value = "公司类型id")
    private String companyTypeId;

    @ApiModelProperty(value = "企业规模")
    private String firmSize;
}
