package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wcwy.company.entity.TJobhunterEducationRecord;
import com.wcwy.company.entity.TJobhunterExpectPosition;
import com.wcwy.company.entity.TJobhunterWorkRecord;
import com.wcwy.company.po.CityPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: ReferrerRecordJobHunterDTO
 * Description:
 * date: 2023/5/23 14:56
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "推荐总数据详情")
public class ReferrerRecordJobHunterDTO {
    /**
     * 推荐人记录id
     */
    @ApiModelProperty(value = "推荐人记录id")
    private Long referrerRecordId;

    /**
     * 求职者id
     */
    @ApiModelProperty(value = "求职者id")
    private String tJobHunterId;

    /**
     * 推荐官id
     */
    @ApiModelProperty(value = "推荐官id")
    private String recommendId;

    @ApiModelProperty(value = "是否下载(0:未下载 1:已下载)")
    private Integer downloadIf;
    /**
     * 推荐官id
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime downloadTime;
    /**
     * 推荐官id
     */
    @ApiModelProperty(value = "人才来源(0:分享引流 1:人才委托 2新增人才 3应聘简历)")
    private Integer correlationType;

    /**
     * 我的推荐
     */
    @ApiModelProperty(value = "我的推荐")
    private Long referrer;

    /**
     * 浏览
     */
    @ApiModelProperty(value = "浏览")
    private Long browse;

    /**
     * 约面
     */
    @ApiModelProperty(value = "约面")
    private Long appoint;

    /**
     * 面试
     */
    @ApiModelProperty(value = "面试")
    private Long interview;

    /**
     * offer
     */
    @ApiModelProperty(value ="offer" )
    private Long offer;
    @ApiModelProperty(value ="联系电话" )
    private String phone;
    /**
     * 入职
     */
    @ApiModelProperty(value = "入职")
    private Long entry;

    /**
     * 淘汰
     */
    @ApiModelProperty(value = "淘汰")
    private Long weedOut;

    /**
     * 待反馈
     */
    @ApiModelProperty(value = "待反馈")
    private Long feedback;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;


    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String userName;
    /**
     * 现住地址
     */
    @ApiModelProperty(value = "现住地址")
    private CityPO address;
    /**
     * 头像路径
     */
    @ApiModelProperty(value = "头像路径")
    private String avatar;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private Integer sex;


    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty(value = "是否显示先生/女士（0不显示 1:显示）")
    private Integer showSex;
    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    private String education;


    /**
     * 目前年薪
     */
    @ApiModelProperty(value = "目前年薪")
    private BigDecimal currentSalary;



    /**
     * 生日
     */

    @ApiModelProperty(value = "生日")
    private LocalDate birthday;

    /**
     * 参加工作时间
     */
    @ApiModelProperty(value = "参加工作时间")
    private LocalDate workTime;

    /**
     * 个人优势
     */

    @ApiModelProperty("优势亮点")
    private List<String> advantage;
    /**
     * 简历附件路径
     */
    @ApiModelProperty(value = "简历附件路径")
    private String resumePath;


    @ApiModelProperty(value = "登录时间")
    private LocalDateTime loginTime;

    @ApiModelProperty("教育经历")
    private List<TJobhunterEducationRecord> record;
    @ApiModelProperty("求职期望")
    private List<TJobhunterExpectPosition> position;
    @ApiModelProperty("工作经历")
    private List<TJobhunterWorkRecord> workRecord;

    @ApiModelProperty(value = "推荐报告")
    private Object report;

    @ApiModelProperty("简历id")
    private String resumeId;
    @ApiModelProperty("是否投简")
    private Boolean sendAResume;
}
