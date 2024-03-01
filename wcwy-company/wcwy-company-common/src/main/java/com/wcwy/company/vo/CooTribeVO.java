package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * ClassName: CooTribeVO
 * Description:
 * date: 2024/1/19 11:37
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("coo发帖表")
public class CooTribeVO {

    /**
     * 标题
     */
    @ApiModelProperty("标题")

    private String title;

    /**
     * 省
     */
    @ApiModelProperty("省")
    @NotNull(message = "省不能为空！")
    private String province;

    /**
     * 类型(0:文章 1:问答 2:心得 3回答)
     */
    @ApiModelProperty("类型(0:文章 1:问答 2:心得 3回答)")
    @NotNull(message = "请选择类型")
    @Max(value = 3,message = "类型错误！")
    @Min(value = 0,message = "类型错误！")
    private Integer type;

    @ApiModelProperty("父亲id")
    private Long father;
    @ApiModelProperty("发帖人")
    private String userId1;

    /**
     * 文案
     */
    @ApiModelProperty("文案")

    private String copyWriter;

    /**
     * 封面
     */
    @ApiModelProperty("封面")
    private String cover;
    @ApiModelProperty("笔名")
    private String pseudonym;

}
