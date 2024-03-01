package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

/**
 * ClassName: TCompanyUpdateVo
 * Description:
 * date: 2022/9/9 10:28
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("企业更新实体类")
public class TCompanyUpdateVo {


    @ApiModelProperty(value = "企业Id")
    @NotBlank(message = "企业id不能为空")
    private String companyId;



    /*@ApiModelProperty(value = "企业名称")
    @NotBlank(message = "企业名称不能为空")
    private String companyName;*/

    /**
     * 企业简称
     */

/*
    @ApiModelProperty(value = "企业简称")
    @NotBlank(message = "企业简称不能为空")
    private String shortName;
*/

    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业)")
    @Max(value = 1,message = "未知企业属性")
    @Min(value=0,message = "未知企业属性")
    @NotNull(message = "请选择企业属性")
    private Integer companyType;


    /**
     * 营业执照
     */

/*
    @ApiModelProperty(value = "营业执照")
    @NotBlank(message = "营业执照不能为空")
    private String businessLicense;
*/

    /**
     * 公司类型id
     */
    @ApiModelProperty(value = "公司类型")
    @NotBlank(message = "公司类型不能为空")
    private String companyTypeId;



    /**
     * 企业LOGO
     */

    @ApiModelProperty(value = "企业LOGO")
    @NotBlank(message = "企业LOGO不能为空")
    private String logoPath;



    /**
     * 企业规模
     */
    @ApiModelProperty(value = "企业规模")
    @NotBlank(message = "企业规模不能为空")
    private String firmSize;

    /**
     * 行业类型
     */

    @ApiModelProperty(value = "行业类型")
    @NotEmpty(message = "企业类型不能为空")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<TIndustryAndTypePO> industry;


}
