package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ClassName: PromotionPostDTO
 * Description:
 * date: 2023/8/29 17:17
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "推广职位数据详情")
public class PromotionPostDTO {

    @ApiModelProperty(value = "推广职位id")
    private String promotionPostId;
    @ApiModelProperty(value = "职位")
    private String postLabel;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "推广次数")
    private Integer count;
    @ApiModelProperty(value = "头像路径")
    private String avatar;
    /**
     * 推广时间
     */
    @ApiModelProperty(value = "推广时间")
    private LocalDateTime promotionTime;
}
