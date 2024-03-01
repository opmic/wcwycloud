package com.wcwy.company.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: JobHunterPostPutInResumeDTO
 * Description:
 * date: 2023/5/29 11:50
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("推荐管理数据")
public class JobHunterPostPutInResumeDTO {
    /**
     * 用户ID
     */
    @ApiModelProperty(value ="用户ID")
    private String userId;


    /**
     * 用户姓名
     */
    @ApiModelProperty(value ="用户姓名" )
    private String userName;
    /**
     * 头像路径
     */
    @ApiModelProperty(value ="头像路径" )
    private String avatar;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty(value ="用户性别（0男 1女 2未知）" )
    private Integer sex;


    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty(value ="是否显示先生/女士（0不显示 1:显示）" )
    private Integer showSex;

    /**
     * 生日
     */
    @ApiModelProperty(value ="生日" )
    private LocalDate birthday;

    /**
     * 参加工作时间
     */
    @ApiModelProperty(value ="参加工作时间" )
    private LocalDate workTime;



    /**
     * 投放简历id
     */
    @ApiModelProperty("投放简历id")
    private String putInResumeId;

    /**
     * 投放的岗位
     */
    @ApiModelProperty("投放的岗位")
    private String putInPost;
    @ApiModelProperty("岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位  4:简历付校园 5:简历付职位)")
    private String postType;


    /**
     * 投简说明
     */
    @ApiModelProperty("投简说明")
    private String explains;

    /**
     * 人选意向
     */
    @ApiModelProperty("人选意向")
    private String intention;

    /**
     * 到岗时间(1:离职-随时到岗 2:在职-2周内到岗 3:在职-1一个月到岗)
     */
    @ApiModelProperty("到岗时间(1:离职-随时到岗 2:在职-2周内到岗 3:在职-1一个月到岗)")
    private Integer arrivalTime;

    /**
     * 目前年薪
     */
    @ApiModelProperty("目前年薪")
    private BigDecimal currentAnnualSalary;

    /**
     * 期望谁前年薪
     */
    @ApiModelProperty("期望谁前年薪")
    private String expectAnnualSalary;

    /**
     * 面试时间(1:可协商 2:提前1天通知 3:期望先电话或视频面试)
     */
    @ApiModelProperty("面试时间(1:可协商 2:提前1天通知 3:期望先电话或视频面试)")
    private Integer applicationInterviewTime;




    /**
     * 投放人
     */
    @ApiModelProperty("投简人消息处理状态(0:无消息 1:已处理) ")
    private Integer putInMessage;


    @ApiModelProperty("投放状态(1:浏览、 2:下载、3排除  4:约面、5:面试中 6取消面试 7淘汰、8:offer、9:入职、10:保证期)")
    private Integer resumeState;



    @ApiModelProperty("是否浏览（0:未操作1：否 2：是）")
    private Integer browseIf;

    /**
     * 浏览时间
     */
    @ApiModelProperty("浏览时间")
    private LocalDateTime browseTime;

    /**
     * 是否下载（0:未操作1：否 2：是）

    @ApiModelProperty("是否下载（0:未操作1：否 2：是）")
    private Integer downloadIf;

    /**
     * 下载时间
     */
    @ApiModelProperty("下载时间")
    private LocalDateTime downloadTime;

    /**
     * 是否排除（0:未操作1：否 2：是）
     */
    @ApiModelProperty("是否排除（0:未操作1：否 2：是）")
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
     * 是否预约（0:未操作1：否 2：是）
     */
    @ApiModelProperty("是否预约（0:未操作1：否 2：是）")
    private Integer subscribeIf;

    /**
     * 是否接受预约（0:未操作1：否 2：是）
     */
    @ApiModelProperty("是否接受预约（0:未操作1：否 2：是）")
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
     * 是否面试中（0:未操作1：否 2：是）
     */
    @ApiModelProperty("是否面试中（0:未操作1：否 2：是）")
    private Integer interviewIf;

    /**
     * 面试时间
     */
    @ApiModelProperty("面试时间")
    private LocalDateTime interviewTime;



    /**
     * 确认面试(0:未操作 1:确认面试)
     */
    @ApiModelProperty("确认面试(0:未操作 1:确认面试)")
    private Integer affirmInterview;
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
     * 是否淘汰（0:未操作1：否 2：是）
     */
    @ApiModelProperty("是否淘汰（0:未操作1：否 2：是）")
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
     * 是否发送offer（0:未操作1：否 2：是）
     */
    @ApiModelProperty("是否发送offer（0:未操作1：否 2：是）")
    private Integer offerIf;

    /**
     * 发送时间
     */
    @ApiModelProperty("发送时间")
    private LocalDateTime offerTime;

    /**
     * 是否入职（0:未操作1：否 2：是）
     */
    @ApiModelProperty("是否入职（0:未操作1：否 2：是）")
    private Integer entryIf;

    /**
     * 未入职原因
     */
    @ApiModelProperty("未入职原因")
    private String entryCause;

    /**
     * 入职时间
     */
    @ApiModelProperty("入职时间")
    private LocalDateTime entryTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;



    /**
     * 岗位名称
     */
    @ApiModelProperty("岗位名称")
    private String postLabel;


    @ApiModelProperty("岗位开始薪资")
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */
    @ApiModelProperty("岗位结束薪资")
    private BigDecimal endSalary;

    @ApiModelProperty(value = "企业名称")
    private String companyName;
    @ApiModelProperty(value = "面试阶段(1:处理中 2:已接受 3:不接受 4取消面试 5面试未通过 6面试通过 7淘汰)")
    private Integer stage;
    @ApiModelProperty(value ="未入职状态(1:待确认2客服介入3:未入职) " )
    private Integer notEntry=0;
    /**
     * 是否过保(0:未操作1:否 2:是)
     */
    @TableField(value = "full_moon_if")
    @ApiModelProperty("是否满月(0:未操作1:否 2:是)")
    private Integer fullMoonIf;

    @TableField(value = "not_full_moon")
    @ApiModelProperty("未满月状态(1待确认 2客服介入 3未满月)")
    private Integer notFullMoon;
    /**
     * 未满月原因
     */
    @TableField(value = "full_moon_cause")
    @ApiModelProperty("未满月原因")
    private String fullMoonCause;


    @TableField(value = "materials",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("未满月举证")
    private List<String> materials;

    /**
     * 离职时间
     */
    @TableField(value = "dimission_time")
    @ApiModelProperty("离职时间")
    private LocalDateTime dimissionTime;


    @ApiModelProperty("处理入职时间")
    private LocalDateTime disposeEntryTime;
    /**
     * 更新时间
     */

    @ApiModelProperty("处理满月时间")
    private LocalDateTime disposeFullMoonTime;


    @ApiModelProperty("是否结算(0:未操作1:否 2:结算中 3完成结算 4:未完成)")
    private Integer closeAnAccountIf;

    @ApiModelProperty("结算开始日期")
    private LocalDateTime closeAnAccountBeginTime;

    @ApiModelProperty("结算完成日期")
    private LocalDateTime closeAnAccountFinishTime;


    @ApiModelProperty("未结算原因")
    private String closeAnAccountCause;
}
