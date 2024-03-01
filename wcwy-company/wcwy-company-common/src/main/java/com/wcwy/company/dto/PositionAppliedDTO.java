package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wcwy.company.po.CityPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClassName: PositionAppliedDTO
 * Description:
 * date: 2023/8/30 16:31
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "人才库数据详情")
public class PositionAppliedDTO {
    /**
     * 来源(0自主注册 1:他人引入)
     */
    @ApiModelProperty(value = "来源(0自主注册 1:他人引入)")
    private Integer source;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "企业id")
    private String companyId;
    /**
     * 意向求职者
     */
    @ApiModelProperty(value = "意向求职者")
    private String jobHunter;

    /**
     * 意向岗位
     */
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;

    @ApiModelProperty(value = "岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付)")
    private Integer postType;


    /**
     * 推广时间
     */
    @ApiModelProperty(value = "推广时间")
    private LocalDateTime promotionTime;

    /**
     * 投简时间
     */
    @ApiModelProperty(value = "投简时间")
    private LocalDateTime putTime;



    @ApiModelProperty(value ="用户姓名" )
    private String userName;

    /**
     * 头像路径
     */
    @ApiModelProperty(value ="头像路径" )
    private String avatar;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty(value ="用户性别（0男 1女 2未知）" )
    private Integer sex;


    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty(value ="是否显示先生/女士（0不显示 1:显示）" )
    private Integer showSex;


    @ApiModelProperty(value ="学历" )
    private String education;


    @ApiModelProperty(value ="生日" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @ApiModelProperty(value ="参加工作时间" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate workTime;

    @ApiModelProperty(value ="费用" )
    private BigDecimal money;

    @ApiModelProperty(value ="类型(0:人民币 1:金币)" )
    private Integer type;

    @ApiModelProperty(value ="是否下载(0:未下载 1:已下载)" )
    private Integer downloadIf;

    @ApiModelProperty(value = "下载时间")
    private LocalDateTime downloadTime;
}
