package com.wcwy.company.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: ShareDataDTO
 * Description:
 * date: 2023/9/1 15:41
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("运营数据")
public class ShareDataDTO {

    @ApiModelProperty("职位数")
    private Map<String, Double> postCount;
    @ApiModelProperty("访问量")
    private Map<String, Double> secondCount;
    @ApiModelProperty("ip数")
    private Map<String, Double> ipAddressCount;
    @ApiModelProperty("访问时长")
    private Map<String, Double> visitCount;
    @ApiModelProperty("注册")
    private Map<String, Double> registerCount;
    @ApiModelProperty("下载")
    private Map<String, Double> downloadCount;

    /*   @ApiModelProperty("A")
       private Integer A;
       @ApiModelProperty("B")
       private Integer B;
       @ApiModelProperty("C")
       private Integer C;
       @ApiModelProperty("D")
       private Integer D;
       @ApiModelProperty("E")
       private Integer E;
       @ApiModelProperty("F")
       private Integer F;*/
    /*    @ApiModelProperty("G")
        private Integer G;*/
    @ApiModelProperty("当前选择")
    Map<String, Map<String, Long>> atPresent=new HashMap<>(7);
    @ApiModelProperty("上一级")
    Map<String, Map<String, Long>> superior =new HashMap<>(7);
    @ApiModelProperty("总共条数")
    private long total;
    /*    @ApiModelProperty("总共条数")
        private long size;*/
    @ApiModelProperty("当前条数")
    private long current;

    @ApiModelProperty("接单")
    private Map<String, Long> orderTaking;
    @ApiModelProperty("入职")
    private Map<String, Long> entry;

    @ApiModelProperty("投简量")
    private Map<String, Long> slip;
    @ApiModelProperty("浏览")
    private Map<String, Long> browse;
    @ApiModelProperty("下载")
    private Map<String, Long> download;
    @ApiModelProperty("offer")
    private Map<String, Long> offer;
    @ApiModelProperty("满月")
    private Map<String, Long> fullMoon;
    @ApiModelProperty("面试")
    private Map<String, Long> interview;
    @ApiModelProperty("上一级投简量")
    private Map<String, Long> superiorSlip;
    @ApiModelProperty("上一级接单")
    private Map<String, Long> superiorOrderTaking;
    @ApiModelProperty("上一级浏览")
    private Map<String, Long> superiorBrowse;
    @ApiModelProperty("上一级面试")
    private Map<String, Long> superiorInterview;
    @ApiModelProperty("上一级下载")
    private Map<String, Long> superiorDownload;
    @ApiModelProperty("上一级offer")
    private Map<String, Long> superiorOffer;
    @ApiModelProperty("上一级入职")
    private Map<String, Long>  superiorEntry;
    @ApiModelProperty("上一级满月")
    private Map<String, Long> superiorFullMoon;

    @ApiModelProperty("接单统计")
    private Long orderTakingSUM;
    @ApiModelProperty("上级接单统计")
    private Long superiorsOrderTakingSUM;

    @ApiModelProperty("投简统计")
    private Long slipSUM;
    @ApiModelProperty("上级投简统计")
    private Long superiorsSlipSUM;

    @ApiModelProperty("浏览统计")
    private Long browseSUM;
    @ApiModelProperty("上级浏览统计")
    private Long superiorsBrowseSUM;

    @ApiModelProperty("Offer统计")
    private Long offerSUM;
    @ApiModelProperty("上级Offer统计")
    private Long superiorsOfferSUM;

    @ApiModelProperty("满月统计")
    private Long fullMoonSUM;
    @ApiModelProperty("上级满月统计")
    private Long superiorsFullMoonSUM;

    @ApiModelProperty("面试统计")
    private Long interviewSUM;
    @ApiModelProperty("上级面试统计")
    private Long superiorsInterviewSUM;


    @ApiModelProperty("下载统计")
    private Long downloadSUM;
    @ApiModelProperty("上级下载统计")
    private Long superiorsDownloadSUM;

    @ApiModelProperty("入职统计")
    private Long entrySUM;
    @ApiModelProperty("上级入职统计")
    private Long superiorsEntrySUM;
}
