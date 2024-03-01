package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.entity.InterviewResume;
import com.wcwy.company.entity.TJobhunterEducationRecord;
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
 * ClassName: selectCompanyResumeDTO
 * Description:
 * date: 2023/4/3 10:49
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "企业查询投放的简历")
public class SelectCompanyResumeDTO {
    /**
     * 投放简历id
     */
    @ApiModelProperty("投放简历id")
    private String putInResumeId;
    /**
     * 投简说明
     */
    @TableField(value = "explains")
    @ApiModelProperty("投简说明")
    private String explains;

    /**
     * 人选意向
     */
    @TableField(value = "intention")
    @ApiModelProperty("人选意向")
    private String intention;

    /**
     * 到岗时间(1:离职-随时到岗 2:在职-2周内到岗 3:在职-1一个月到岗)
     */
    @TableField(value = "arrival_time")
    @ApiModelProperty("到岗时间(1:离职-随时到岗 2:在职-2周内到岗 3:在职-1一个月到岗)")
    private Integer arrivalTime;

    /**
     * 目前年薪
     */
    @TableField(value = "current_annual_salary")
    @ApiModelProperty("目前年薪")
    private BigDecimal currentAnnualSalary;

    /**
     * 期望谁前年薪
     */
    @TableField(value = "expect_annual_salary")
    @ApiModelProperty("期望谁前年薪")
    private String expectAnnualSalary;

    /**
     * 面试时间(1:可协商 2:提前1天通知 3:期望先电话或视频面试)
     */
    @TableField(value = "application_interview_time")
    @ApiModelProperty("面试时间(1:可协商 2:提前1天通知 3:期望先电话或视频面试)")
    private Integer applicationInterviewTime;
    /**
     * 投放的岗位
     */
    @ApiModelProperty("投放的岗位")
    private String putInPost;

    @ApiModelProperty(value = "生日")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    /**
     * 投放的公司
     */
    @ApiModelProperty("投放的公司")
    private String putInComppany;

    /**
     * 求职者
     */
    @ApiModelProperty("求职者")
    private String putInJobhunter;


    @ApiModelProperty("投简人消息处理状态(0:无消息 1:已处理) ")
    private Integer putInMessage;

    /**
     * 投放人
     */

    @ApiModelProperty("企业消息处理状态(0:无消息 1:已处理)")
    private Integer tCompanyMessage;


/*    *//**
     * 投简说明
     *//*
    @ApiModelProperty("投简说明")
    private String explains;*/
    /**
     * 投放人
     */
    @ApiModelProperty("投放人")
    private String putInUser;

    /**
     * 是的代投(0:不是 1:是)
     */
    @ApiModelProperty("是的代投(0:不是 1:是)")
    private String easco;

    /**
     * 投放状态(1:浏览、 2:下载、3排除  4:约面、5:面试中6淘汰、7:offer、8:入职、9:保证期)
     */
    @ApiModelProperty("投放状态(1:浏览、 2:下载、3排除  4:约面、5:面试中 6取消面试 7淘汰、8:offer、9:入职、10:满月))")
    private Integer resumeState;

    /**
     * 是否浏览（1：否 2：是）
     */
    @ApiModelProperty("是否浏览（1：否 2：是）")
    private Integer browseIf;

    /**
     * 浏览时间
     */
    @ApiModelProperty("浏览时间")
    private LocalDateTime browseTime;

    /**
     * 是否下载（1：否 2：是）
     */
    @ApiModelProperty("是否下载（1：否 2：是）")
    private Integer downloadIf;

    /**
     * 下载时间
     */
    @ApiModelProperty("下载时间")
    private LocalDateTime downloadTime;

    /**
     * 是否排除（1：否 2：是）
     */
    @ApiModelProperty("是否排除（1：否 2：是）")
    private Integer excludeIf;

    /**
     * 排除时间
     */
    @ApiModelProperty("排除时间")
    private LocalDateTime excludeTime;

    /**
     * 排除原因
     */
    @ApiModelProperty("排除原因")
    private String excludeState;

    /**
     * 是否预约（1：否 2：是）
     */
    @ApiModelProperty(" 是否预约（1：否 2：是）")
    private Integer subscribeIf;

    /**
     * 是否接受预约（1：否 2：是）
     */
    @ApiModelProperty("是否接受预约（1：否 2：是）")
    private Integer acceptSubscribe;

    /**
     * 邀请反馈模板
     */
    @ApiModelProperty("邀请反馈模板")
    private String invitation;

    /**
     * 不接受原因
     */
    @ApiModelProperty("不接受原因")
    private String acceptSubscribeState;

    /**
     * 预约时间
     */
    @ApiModelProperty("预约时间")
    private LocalDateTime subscribeTime;

    /**
     * 是否面试中（1：否 2：是）
     */
    @ApiModelProperty("是否面试中（1：否 2：是）")
    private Integer interviewIf;

    /**
     * 面试时间
     */
    @ApiModelProperty("面试时间")
    private LocalDateTime interviewTime;

    /**
     * 是否取消(0:未操作1：否 2：是)
     */
    @ApiModelProperty("是否取消(0:未操作1：否 2：是)")
    private Integer cancelIf;

    /**
     * 取消时间
     */
    @ApiModelProperty("取消时间")
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    @ApiModelProperty("取消原因")
    private Integer cancelCause;



    /**
     *
     */
    @ApiModelProperty("面试邀请表")
    List<InterviewResume> interviewResume;
    /**
     * 是否淘汰（1：否 2：是）
     */
    @ApiModelProperty("是否淘汰（1：否 2：是）")
    private Integer weedOutIf;

    /**
     * 淘汰时间
     */
    @ApiModelProperty("淘汰时间")
    private LocalDateTime weedOutTime;

    /**
     * 淘汰原因
     */
    @ApiModelProperty("淘汰原因")
    private String weedOutCause;

    /**
     * 是否发送offer（1：否 2：是）
     */
    @ApiModelProperty("是否发送offer（1：否 2：是）")
    private Integer offerIf;

    /**
     * 发送时间
     */
    @ApiModelProperty("发送时间")
    private LocalDateTime offerTime;

    /**
     * 是否入职（1：否 2：是）
     */
    @ApiModelProperty("是否入职（1：否 2：是）")
    private Integer entryIf;

    @ApiModelProperty("确认面试(0:未操作 1:确认面试)")
    private Integer affirmInterview;


    @ApiModelProperty("面试时间")
    private LocalDateTime affirmInterviewTime;
    /**
     * 是否过保(0:未操作1:否 2:是)
     */
    @ApiModelProperty("是否满月(0:未操作1:否 2:是)")
    private Integer fullMoonIf;

    @ApiModelProperty("未满月状态(1待确认 2客服介入 3未满月)")
    private Integer notFullMoon;
    /**
     * 未满月原因
     */
    @ApiModelProperty("未满月原因")
    private String fullMoonCause;


    @ApiModelProperty("未满月举证")
    private List<String> materials;

    /**
     * 离职时间
     */
    @ApiModelProperty("离职时间")
    private LocalDateTime dimissionTime;



    @ApiModelProperty("是否结算(0:未操作1:否 2:结算中 3完成结算 4:未完成)")
    private Integer closeAnAccountIf;

    @ApiModelProperty("结算开始日期")
    private LocalDateTime closeAnAccountBeginTime;

    @ApiModelProperty("结算完成日期")
    private LocalDateTime closeAnAccountFinishTime;


    @ApiModelProperty("未结算原因")
    private String closeAnAccountCause;

    /**
     * 入职时间
     */

    @ApiModelProperty("入职时间")
    private LocalDateTime entryTime;


    @ApiModelProperty("未入职原因")
    private String entryCause;

    @ApiModelProperty("是否有消息")
    private Boolean information;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;



    /**
     * 用户ID
     */
    @ApiModelProperty(value = "求职者id")
    private String userId;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "求职者姓名")
    private String userName;
    /**
     * 现住地址
     */
    @TableField(value = "address", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "求职者现住地址")
    private CityPO address;
    /**
     * 头像路径
     */
    @TableField(value = "avatar")
    @ApiModelProperty(value = "求职者头像路径")
    private String avatar;
    /**
     * 用户性别（0男 1女 2未知）
     */
    @TableField(value = "sex")
    @ApiModelProperty(value = "求职者性别（0男 1女 2未知）")
    private Integer sex;
    /**
     * 用户性别（0男 1女 2未知）
     */
    @TableField(value = "show_sex")
    @ApiModelProperty(value = "求职者是否显示先生/女士（0不显示 1:显示）")
    private Integer showSex;
    /**
     * 学历
     */
 /*   @ApiModelProperty(value = "学历")
    private String education;*/

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "参加工作时间")
    private LocalDate workTime;

    @ApiModelProperty(value = "登录时间")
    private LocalDateTime loginTime;

    @ApiModelProperty(value = "期望职位")
    private String positionName;

    @ApiModelProperty(value = "教育经历")
    List<TJobhunterEducationRecord> record;

    @ApiModelProperty(value = "工作经历")
    private List<TJobhunterWorkRecord> workRecord;
    @ApiModelProperty(value = "是否收藏(0未收藏 1收藏)")
    private Integer isCollect;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = " 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位 3到面付 4:简历付校园 5:简历付职位)")
    private String postType;

    @ApiModelProperty(value ="未入职状态(1:待确认2客服介入3:未入职) " )
    private Integer notEntry;

    @ApiModelProperty(value = "岗位名称")
    private String postLabel;


    @ApiModelProperty("处理入职时间")
    private LocalDateTime disposeEntryTime;
    /**
     * 更新时间
     */

    @ApiModelProperty("处理满月时间")
    private LocalDateTime disposeFullMoonTime;
}
