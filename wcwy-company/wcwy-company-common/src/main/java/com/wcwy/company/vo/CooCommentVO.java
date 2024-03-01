package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * ClassName: CooCommentVO
 * Description:
 * date: 2024/1/19 14:41
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel("发表评论")
@Data
public class CooCommentVO {

    /**
     * 帖子id
     */

    @ApiModelProperty("帖子id")
    @NotNull(message = "帖子id不能为空!")
    private Long cooTribeId;
    @ApiModelProperty("发帖人")
    @NotBlank(message = "发帖人不能为空!")
    private String userId;
    /**
     * 回答
     */

    @ApiModelProperty("回答")
    @NotBlank(message = "回答不能为空！")
    private String answer;

    /**
     * 省
     */

    @ApiModelProperty("省")
    @NotBlank(message = "省不能为空！")
    private String province;

}
