package com.wcwy.company.query;


import com.baomidou.mybatisplus.annotation.TableField;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * ClassName: TCompanyQuery
 * Description:
 * date: 2022/9/1 16:48
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "企业查询条件实体类")
public class TCompanyQuery extends PageQuery {
    /**
     * 企业Id
     */
    @ApiModelProperty(value = "企业Id")
    private String companyId;
    /**
     * 手机号码
     */
    @TableField(value = "phone_number")
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    /**
     * 企业名称
     */
    @TableField(value = "company_name")
    @ApiModelProperty(value = "企业名称")
    private String companyName;

}
