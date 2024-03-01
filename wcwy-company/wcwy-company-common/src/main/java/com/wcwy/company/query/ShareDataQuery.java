package com.wcwy.company.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClassName: ShareDataQuery
 * Description:
 * date: 2023/9/1 15:52
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "运营数据查询")
public class ShareDataQuery extends PageQuery {

    @ApiModelProperty("访问类型(0职位分享 1求职者 2猎企 3企业 4职场推荐官 5校园推荐官)")
    private Integer type;


    @ApiModelProperty("职位详情(1:入职付 2:满月付 3:到面付 4:简历付)")
    private Integer postType = 1;
 /*   @ApiModelProperty("省")
    private String province;*/

    /**
     * 市
     */
    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("周开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate beginDate;

    @ApiModelProperty("周结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;


    @ApiModelProperty("年月份")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate month;

    @ApiModelProperty("类型(0:日 1:周 2:月)")
    @NotNull(message = "请选择类型")
    public Integer dateType = 0;

    @ApiModelProperty("日开是时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate day;


    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate allBeginDate;

    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate allEndDate;
    /*
      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
      private LocalDateTime thePreviousDay;*/
    @ApiModelProperty("推荐官id")
    @NotBlank(message = "推荐官id")
    private String userId;
}
