package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wcwy.company.vo.ValidList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 * ClassName: notEntryDTO
 * Description:
 * date: 2023/6/1 14:13
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "未入职查询")
public class NotEntryDTO {
    @ApiModelProperty("岗位开始薪资")
    private BigDecimal beginSalary;
    @ApiModelProperty("岗位结束薪资")
    private BigDecimal endSalary;
    @ApiModelProperty("离职时间")
    private LocalDateTime dimissionTime;
    @ApiModelProperty(value = "入职日期")
    private LocalDate entryDate;

    @ApiModelProperty(value = "入职时间")
    private LocalTime entryTime;
    @ApiModelProperty("岗位名称")
    private String postLabel;
    @ApiModelProperty(value = "企业名称")
    private String companyName;
    @ApiModelProperty("投放简历id")
    private String putInResumeId;
    @ApiModelProperty(value ="用户姓名" )
    private String userName;
    @ApiModelProperty(value = " 取消原因")
    private String cancelCause;
    @ApiModelProperty(value = "联系人")
    private String contactName;
    @ApiModelProperty(value = "联系方式")
    private String contactPhone;
    @ApiModelProperty(value = "投简人是否同意(0:未处理 1:不同意 2:已同意 3客服介入)")
    private Integer putInConsent;
    @ApiModelProperty("未入职原因")
    private String entryCause;
    @ApiModelProperty("处理满月时间")
    private LocalDateTime disposeFullMoonTime;
    @ApiModelProperty("处理入职时间")
    private LocalDateTime disposeEntryTime;
    @ApiModelProperty("未满月原因")
    private String fullMoonCause;
    @ApiModelProperty(value ="未入职状态(1:待确认2客服介入3:未入职) " )
    private Integer notEntry;
    @ApiModelProperty("未满月举证")
    private ValidList<String> materials;
    @ApiModelProperty("未满月状态(1待确认 2客服介入 3未满月)")
    private Integer notFullMoon;
}
