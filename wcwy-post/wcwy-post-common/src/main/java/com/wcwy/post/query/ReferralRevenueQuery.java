package com.wcwy.post.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * ClassName: ReferralRevenueQuery
 * Description:
 * date: 2023/8/10 16:02
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Data
@ApiModel("推荐收益查询")
public class ReferralRevenueQuery extends PageQuery {

    @ApiModelProperty("企业名称")
    private String companyName;
    @ApiModelProperty("岗位名称")
    private String postLabel;
    @ApiModelProperty("岗位类型(0:全部 1入职服 2满月付3：到面付)")
    private String type;
    @ApiModelProperty("日期(yyyy-MM)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private Date date;
    @ApiModelProperty("关键字")
    private String keyword;

    private String year;
    private String month;
    private String userId;
}
