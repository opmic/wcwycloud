package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * ClassName: addContract
 * Description:
 * date: 2022/11/16 14:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("企业添加合同")
public class AddContractVO {
    /**
     * 企业Id
     */
    @ApiModelProperty(value = "合同id",required = true)
    @NotBlank(message = "合同id不能为空!")
    private String contractId;

    /**
     * 签约合同
     */
    @ApiModelProperty(value = "合同地址",required = true)
    @Valid
    @NotNull(message = "合同地址不能为空!")
    private ValidList<String> signContract;

}
