package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 推荐官企业表
 * @TableName t_recommended_companies
 */
@Data
@ApiModel("推荐官企业修改密码")
public class TRecommendedCompaniesUpdatePasswordVO  {
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(max = 18,min = 6 ,message = "密码要求6-18位")
    private String password;

    @ApiModelProperty(value = "旧密码")
    @NotBlank(message = "旧密码不能为空")
    @Size(max = 18,min =6  ,message = "旧密码要求6-18位")
    private String oldPassword;
}