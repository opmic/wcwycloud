package com.wcwy.post.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wcwy.post.entity.ApplyForInvoice;
import com.wcwy.post.po.DetailedAddress;
import com.wcwy.post.po.ProvincesCitiesPO;
import com.wcwy.post.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ClassName: PostCollectDTO
 * Description:
 * date: 2023/2/15 8:54
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("查看在招职位")
public class PostCollectDTO  {
    /**
     * 发布岗位Id
     */
    @ApiModelProperty(value = "发布岗位Id")
    private String postId;

    /**
     * 企业Id
     */
    @ApiModelProperty(value = "企业Id")
    private String companyId;


    /**
     * 企业logo
     */
    @ApiModelProperty(value = "企业logo")
    private String logo;
    @ApiModelProperty(value = "自定义logo")
    private String customLogo;

    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业)")
    private Integer companyType;
    /**
     * 所在城市
     */
    @ApiModelProperty(value = "所在城市")
    private ProvincesCitiesPO workCity;


    /**
     * 职位福利
     */
    @ApiModelProperty(value = "职位福利")
    private List<String> postWealId;


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
     * 工作经验
     */
    @ApiModelProperty(value = "工作经验")
    private String workExperience;


    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    private String educationType;


    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;


    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */
    @ApiModelProperty(value = "岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位 4:简历付校园 5:简历付职位)")
    private Integer postType;

    @ApiModelProperty(value = "是否收藏岗位")
    private Boolean collect;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime createTime;

}
