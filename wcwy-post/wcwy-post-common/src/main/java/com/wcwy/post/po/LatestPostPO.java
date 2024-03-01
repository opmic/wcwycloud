package com.wcwy.post.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ClassName: LatestPostPO
 * Description:
 * date: 2023/2/14 11:21
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "公司最新职位")
public class LatestPostPO {
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
    @ApiModelProperty(value = "所在城市")
    private ProvincesCitiesPO workCity;
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

    @ApiModelProperty(value = "岗位名称")
    private String postLabel;

    /**
     * 岗位开始薪资
     */

    @ApiModelProperty(value = "岗位开始薪资")
    private BigDecimal beginSalary;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime dayTime;
    /**
     * 岗位结束薪资
     */
    @ApiModelProperty(value = "岗位结束薪资")
    private BigDecimal endSalary;
}
