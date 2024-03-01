package com.wcwy.company.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * ClassName: PostDataQuery
 * Description:
 * date: 2023/11/22 15:17
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("推广职位查询")
public class PostDataQuery {

    @ApiModelProperty("访问类型(0职位数 1访问量 2IP数 3平均时长 4简历注册 5简历下载)")
    private Integer type;




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
    public Integer dateType=0;

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

    private String userId;

}
