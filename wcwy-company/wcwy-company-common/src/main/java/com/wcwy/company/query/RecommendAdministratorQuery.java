package com.wcwy.company.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;

/**
 * ClassName: RecommendAdministratorQuery
 * Description:
 * date: 2023/5/29 11:40
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("推荐管理查询")
public class RecommendAdministratorQuery  extends PageQuery {

    @ApiModelProperty(value = "企业名称")
    private String companyName;
    @ApiModelProperty(value = "职位名称")
    private String postLabel;
    //@ApiModelProperty("投放状态(0:待浏览 1:已浏览、 2:约面、3:面试中  4:offer、5:入职、6:未入职 7淘汰)")
    @ApiModelProperty("投放状态(0:待浏览 1:已浏览、2:下载 3:约面、4:面试中  5:offer、6:入职、7:未入职 8:满月,9:未满月10淘汰)")
    @Max(value = 10,message = "操作错误!")
    @Max(value = 0,message = "操作错误!")
    private Integer resumeState;
    @ApiModelProperty("关键字")
    private  String keyword;
    @ApiModelProperty("岗位发布类型(0普通改为 1:入职付 2满月付 3到面付 3:简历付校 4:简历付值)")
    private  Integer type;

}
