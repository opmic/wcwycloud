package com.wcwy.company.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.po.ProvincesCitiesPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ClassName: HistorySubmitAResumeDTO
 * Description:
 * date: 2023/6/8 17:15
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel("推荐历史数据")
@Data
public class HistorySubmitAResumeDTO {
    /**
     * 投放简历id
     */
    @ApiModelProperty("投放简历id")
    private String putInResumeId;



    @ApiModelProperty(value = "岗位开始薪资")
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */
    @ApiModelProperty(value = "岗位结束薪资")
    private BigDecimal endSalary;


    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;
    /**
     * 岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付)
     */
    @ApiModelProperty(value = "岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付)")
    private Integer postType;


    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String companyName;
    /**
     * 企业属性(0招聘企业 1猎头企业)
     */

    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业)")
    private Integer companyType;

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
     * 是否入职（0:未操作1：否 2：是）
     */
    @ApiModelProperty("是否入职（0:未操作1：否 2：是）")
    private Integer entryIf;





    /**
     * 是否结算(0:未操作1:否 2:是)
     */
    @ApiModelProperty("是否结算(0:未操作1:否 2:结算中 3完成结算 4:未完成)")
    private Integer closeAnAccountIf;





    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;




    @ApiModelProperty(value ="未入职状态(1:待确认2客服介入3:未入职) " )
    private Integer notEntry;
}
