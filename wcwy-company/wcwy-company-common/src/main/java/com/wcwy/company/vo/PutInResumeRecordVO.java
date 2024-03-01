package com.wcwy.company.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * ClassName: PutInResumeRecordVO
 * Description:
 * date: 2023/5/15 11:11
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("记录投简记录")
public class PutInResumeRecordVO {
    @ApiModelProperty(value = "身份(0:企业 1:推荐官 2:求职者)")
    private Integer identity;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "时间")
    private Date date;
    @ApiModelProperty(value = "投放状态(0:投简1:浏览、 2:下载、3排除  4:约面、5:接受面试 6:不接受面试 7:面试修改  8:面试通过 9:淘汰 10:取消面试 11:offer、12:已入职、13:未入职、14:满月、15:未满月)")
    private Integer state;
    @ApiModelProperty(value = "原因")
    private  String cause;
}
