package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: ReferrerRecordDTO
 * Description:
 * date: 2022/12/28 11:13
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Data
@ApiModel(value = "推荐总数据")
public class  ReferrerRecordDTO {
    @ApiModelProperty("我的人才")
    private Long TJobHunterSUM;
    /**
     * 我的推荐
     */
    @ApiModelProperty(value = "我的推荐")
    private Long referrer=0L;

    /**
     * 浏览
     */
    @ApiModelProperty(value = "浏览")
    private Long browse=0L;

    /**
     * 约面
     */
    @ApiModelProperty(value = "约面")
    private Long appoint=0L;

    /**
     * 面试
     */
    @ApiModelProperty(value = "面试")
    private Long interview=0L;

    /**
     * offer
     */
    @ApiModelProperty(value = "offer")
    private Long offer=0L;

    /**
     * 入职
     */
    @ApiModelProperty(value = "入职")
    private Long entry=0L;

    /**
     * 淘汰
     */
    @ApiModelProperty(value = "淘汰")
    private Long weedOut=0L;

    /**
     * 待反馈
     */
    @ApiModelProperty(value = "待反馈")
    private Long feedback=0L;



    @ApiModelProperty(value = "我的下载")
    private Long download=0L;


    @ApiModelProperty(value = "我的人才")
    private Long talents;
    @ApiModelProperty(value = "推荐人选")
    private Long personSelected;

    @ApiModelProperty(value = "接单岗位")
    private Integer post;
    @ApiModelProperty(value = "收藏崗位")
    private Integer collectPost;


    @ApiModelProperty("我的崗位")
    private  Integer minePost;

    @ApiModelProperty("我的崗位")
    private  Integer mineOnlinePost;

    @ApiModelProperty("应聘简历")
    private Integer interviewResume;

    @ApiModelProperty("未浏览")
    private Integer notViewed;
}
