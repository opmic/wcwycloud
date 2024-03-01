package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.po.ProvincesCitiesPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: PutInResumeParticularsDTO
 * Description:
 * date: 2023/5/31 15:53
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "投简详情")
public class PutInResumeParticularsDTO {
    /**
     * 投放简历id
     */
    @ApiModelProperty("佣金率")
    private  Integer commission;

    @ApiModelProperty("佣金")
    private  String  divideInto;
    @ApiModelProperty("投放简历id")
    private String putInResumeId;

    /**
     * 投放的岗位
     */
    @ApiModelProperty("投放的岗位")
    private String putInPost;
    @ApiModelProperty(value = "企业名称")
    private String companyName;
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

    /**
     * 投简说明
     */
    @ApiModelProperty("推荐原因")
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
     * 投放状态(1:浏览、 2:下载、3排除  4:约面、5:面试中6淘汰、7:offer、8:入职、9:保证期)
     */
    @ApiModelProperty("投放状态(1:浏览、 2:下载、3排除  4:约面、5:面试中 6取消面试 7淘汰、8:offer、9:入职、10:保证期)")
    private Integer resumeState;

    /**
     * 是否浏览（0:未操作1：否 2：是）
     */
    @ApiModelProperty("是否浏览（0:未操作1：否 2：是）")
    private Integer browseIf;

    /**
     * 浏览时间
     */
    @ApiModelProperty("浏览时间")
    private LocalDateTime browseTime;

    /**
     * 是否下载（0:未操作1：否 2：是）
     */
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


    @ApiModelProperty("面试时间")
    private LocalDateTime affirmInterviewTime;
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

    /**
     * 是否结算(0:未操作1:否 2:是)
     */
    @ApiModelProperty("是否结算(0:未操作1:否 2:结算中 3完成结算 4:未完成)")
    private Integer closeAnAccountIf;

    /**
     * 结算开始日期
     */
    @ApiModelProperty("结算开始日期")
    private LocalDateTime closeAnAccountBeginTime;

    /**
     * 结算完成日期
     */
    @ApiModelProperty("结算完成日期")
    private LocalDateTime closeAnAccountFinishTime;

    /**
     * 未结算原因
     */
    @ApiModelProperty("未结算原因")
    private String closeAnAccountCause;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;



    /**
     * 岗位开始薪资
     */

    @ApiModelProperty("岗位开始薪资")
    private BigDecimal beginSalary;
    /**
     * 岗位结束薪资
     */
    @ApiModelProperty("岗位结束薪资")
    private BigDecimal endSalary;


    /**
     * 岗位名称
     */
    @ApiModelProperty("岗位名称")
    private String postLabel;
    @ApiModelProperty(value = "求职者信息")
    private RmJobHunterDTO rmJobHunterDTO;

    @ApiModelProperty(" 岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付)")
    private Integer postType;

    @ApiModelProperty("隐藏显示二维码")
    private Boolean isShow=false;
    @ApiModelProperty(value ="未入职状态(1:待确认2客服介入3:未入职) " )
    private Integer notEntry;

    @ApiModelProperty(value = "面试阶段(1:处理中 2:已接受 3:不接受 4取消面试 5面试未通过 6面试通过 7淘汰)")
    private Integer stage;
}
