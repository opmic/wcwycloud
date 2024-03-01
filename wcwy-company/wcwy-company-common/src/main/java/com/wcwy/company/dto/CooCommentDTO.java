package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName: CooCommentDTO
 * Description:
 * date: 2024/1/19 14:28
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("获取评论")
public class CooCommentDTO {
    /**
     *
     */

    @ApiModelProperty("评论id")
    private Long id;

    /**
     * 帖子id
     */

    @ApiModelProperty("帖子id")
    private Long cooTribeId;

    /**
     * 回答
     */

    @ApiModelProperty("回答")
    private String answer;

    /**
     * 省
     */

    @ApiModelProperty("省")
    private String province;

    /**
     * 创建时间
     */

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 评论人
     */

    @ApiModelProperty("评论人")
    private String createUser;

    /**
     * 姓名
     */
    @TableField(value = "username")
    @ApiModelProperty("姓名")
    private String username;

    @ApiModelProperty("头像地址")
    private String headPath;

    @ApiModelProperty("性别:(1:男 2:女生)")
    private Integer sex;
}
