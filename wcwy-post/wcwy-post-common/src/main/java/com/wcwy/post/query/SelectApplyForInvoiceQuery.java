package com.wcwy.post.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: selectApplyForInvoiceQuery
 * Description:
 * date: 2022/12/1 17:15
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("查询发票")
public class SelectApplyForInvoiceQuery extends PageQuery {
    /**
     * 申请发票id
     */
    @ApiModelProperty(value = "申请发票id",required = false)
    private String applyForInvoiceId;

    /**
     * 处理状态(0:申请中 1:申请成功 2:申请失败 3:取消申请)
     */
    @ApiModelProperty(value = " 处理状态(0:申请中 1:申请成功 2:申请失败 3:取消申请)",required = false)
    private Integer processState;
    /**
     * 发票号码
     */
    @ApiModelProperty(value = "发票号码",required = false)
    private String number;
    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate beginTime;


    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endTime;


    @ApiModelProperty(" 发票性质(0未知1:纸质 2:电子)")
    private Integer type;
    @ApiModelProperty("发票类型(1:增值税普通发票(电子),2增值税专用发票(纸质))")
    private Integer invoiceType;
}
