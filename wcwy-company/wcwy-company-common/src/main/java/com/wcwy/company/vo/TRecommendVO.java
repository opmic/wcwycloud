package com.wcwy.company.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ClassName: TRecommendDTO
 * Description:
 * date: 2022/9/7 16:19
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "推荐官表完善信息表")
public class TRecommendVO {

    /**
     * id
     */
    @ApiModelProperty("推荐官id")
    @NotBlank(message = "id不能为空!")
    private String id;

    /**
     * 姓名
     */
    @ApiModelProperty("联系人")
    @NotBlank(message = "联系人不能为空!")
    private String username;
    /**
     * 出生年月日
     */
    @ApiModelProperty("出生年月日")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "出生年月日不能为空!")
    private LocalDate birth;

    /**
     * 头像地址
     */
    @ApiModelProperty("头像地址")
    @NotBlank(message = "头像地址不能为空!")
    private String headPath;

    /**
     * 学历
     */
    @ApiModelProperty("学历")
    @NotBlank(message = "学历不能为空!")
    private String education;

    /**
     * 性别:(1:男 2:女生)
     */
    @ApiModelProperty("性别:(1:男 2:女生)")
    @NotNull(message = "请选择性别!")
    private Integer sex;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系电话")
    @NotBlank(message = "联系电话不能为空!")
    private String phone;





    /**
     * 自我介绍
     */
    @ApiModelProperty("自我介绍")
    @NotBlank(message = "自我介绍不能为空!")
    private String manMessage;

    /**
     * 院校名称
     */
    @ApiModelProperty("院校名称")
    @NotBlank(message = "院校名称不能为空!")
    private String academy;

    /**
     * 专业
     */
    @ApiModelProperty("专业")
    @NotBlank(message = "专业不能为空!")
    private String careerId;

    /**
     * 入学时间
     */
    @ApiModelProperty("入学时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "入学时间不正确")
    private LocalDate entranceTime;

    /**
     * 毕业时间
     */
    @ApiModelProperty("毕业时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "毕业时间不正确")
    private LocalDate graduateTime;


    @ApiModelProperty(value = "擅长行业")
    private List<TIndustryAndTypePO> industry;

    @ApiModelProperty(value = "推荐职位所在城市")
    @NotEmpty (message = "推荐职位所在城市不能为空")
    private List<ProvincesCitiesPO> recommendedCity;

    /**
     * 身份(0:职场基因 1：校园基因)
     */
    @ApiModelProperty("身份(0:职场精英 1：校园英才)")
    @NotNull(message = "请选择您的身份!")
    private Integer identity;

    @ApiModelProperty(value = "所住地")
    @Valid
    private ProvincesCitiesPO address;


}
