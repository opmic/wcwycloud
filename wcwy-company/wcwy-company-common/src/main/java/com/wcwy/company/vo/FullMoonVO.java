package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * ClassName: FullMoonVO
 * Description:
 * date: 2023/9/15 9:38
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("未满月操作")
public class FullMoonVO {


    @ApiModelProperty("未满月")
    @NotBlank(message = "未满月不能为空!")
    private String fullMoonCause;


    @ApiModelProperty("未满月举证")
    @Valid
    private ValidList<String> materials;

    @ApiModelProperty("投放简历id")
    @NotBlank(message = "投放简历id不能为空!")
    private String putInResumeId;
}
