package com.wcwy.company.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName: CooTribeDTO
 * Description:
 * date: 2024/1/19 13:44
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("发帖1")
public class CooTribeDTO {
    /**
     * id
     */

    @ApiModelProperty("发帖id")
    private Long id;

    /**
     * 标题
     */

    @ApiModelProperty("标题")
    private String title;

    /**
     * 省
     */

    @ApiModelProperty("省")
    private String province;

    /**
     * 类型(0:文章 1:问答 2:心得 3回答)
     */

    @ApiModelProperty("类型(0:文章 1:问答 2:心得 3回答)")
    private Integer type;

    /**
     * 父亲id
     */

    @ApiModelProperty("父亲id")
    private Long father;

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

    /**
     * 创建时间
     */

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;





    /**
     * 失败原因
     */

    @ApiModelProperty("失败原因")
    private String causeOfFailure;

    /**
     * 创建人
     */

    @ApiModelProperty("创建人")
    private String userId;

    /**
     * 浏览量
     */

    @ApiModelProperty("浏览量")
    private Long browse;

    /**
     * 点赞量
     */

    @ApiModelProperty("点赞量")
    private Long zan;

    /**
     * 评论量
     */

    @ApiModelProperty("评论量")
    private Long comment;

    /**
     * 分享量
     */

    @ApiModelProperty("分享量")
    private Long share;

    /**
     * 收藏量
     */

    @ApiModelProperty("收藏量")
    private Long collect;


    @ApiModelProperty("姓名")
    private String username;

    @ApiModelProperty("头像地址")
    private String headPath;

    @ApiModelProperty("性别:(1:男 2:女生)")
    private Integer sex;

    @ApiModelProperty("是否点赞")
    private boolean booleanZan;
    @ApiModelProperty("笔名")
    private String pseudonym;
    @ApiModelProperty("是否收藏")
    private boolean booleanCollect;

    @ApiModelProperty("是否")
    private boolean reply;
}
