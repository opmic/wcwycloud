package com.wcwy.company.vo;

import com.wcwy.company.po.ProvincesCitiesPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * ClassName: TCompanyWelfareVO
 * Description:
 * date: 2023/3/27 9:09
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("企业添加工作地址及福利")
public class TCompanyWelfareVO {
    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址",required = true)
    @NotBlank(message = "详细地址不能为空")
    private String address;

    @ApiModelProperty(value = "所在省市",required = true)
    @Valid
    private ProvincesCitiesPO provincesCities;
    @ApiModelProperty(value = "所在省市")
    public List<String> welfare;
}
