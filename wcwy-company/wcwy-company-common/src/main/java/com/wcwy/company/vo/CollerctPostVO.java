package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 岗位收藏表
 *
 * @TableName collerct_post
 */
@Data
@ApiModel("岗位收藏表")
public class CollerctPostVO implements Serializable {


    /**
     * 收藏的岗位
     */
    @ApiModelProperty("收藏的岗位")
    @NotEmpty (message = "收藏的岗位不能为空!")
    private List<String> post;

    /**
     * 身份(1:求职者 2:推荐官)
     */
    @ApiModelProperty("身份(1:求职者 2:推荐官)")
    @NotNull(message = "身份不能为空")
    private Integer identity;

    /**
     * 收藏岗位用户id
     */
    @ApiModelProperty("收藏岗位用户id")
    @NotBlank(message = "收藏岗位用户身份不能为空")
    private String collerctUserId;


}