package com.wcwy.post.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * ClassName: PositionQuery
 * Description:
 * date: 2023/2/15 15:44
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Data
@ApiModel("职位筛选")
public class PositionQuery extends PageQuery {

    @ApiModelProperty(value = "关键字")
    private String keyword;
    /**
     * 企业Id
     */
    @TableField(value = "company_id")
    @ApiModelProperty(value = "企业Id")
    private String companyId;



    @ApiModelProperty(value = "求职者id")
    private String JobHunterId;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称或者职位名称")
    private String companyName;


    /**
     * 企业规模
     */
    @ApiModelProperty(value = "企业规模")
    private String firmSize;



    /**
     * 职位类别
     */
    @ApiModelProperty(value = "职位类别")
    private List<String> position;



    /**
     * 职位福利
     */

    @ApiModelProperty(value = "职位福利")
    private List<String> postWealId;

    @ApiModelProperty(value = "年薪范围")
   private Integer annualSalary;

    /**
     * 岗位开始薪资
     */
    @ApiModelProperty(value = "岗位开始薪资 注:不用传")
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */
    @ApiModelProperty(value = "岗位结束薪资 注:不用传")
    private BigDecimal endSalary;

    /**
     * 工作经验
     */
    @ApiModelProperty(value = "工作经验")
    private List<Integer> workExperience;


    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    private List<Integer> educationType;

    /**
     * 工作城市
     */
    @ApiModelProperty(value = "工作城市")
    @Size(max = 3,message = "工作城市选择不能超过三个")
    private List<String> city;
    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */
/*    @ApiModelProperty(value = " 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)")
    private Integer postType;*/
    @ApiModelProperty(value = "工作性质")
    private String jobCategory;
/*    @ApiModelProperty(value = "岗位状态(0：停止招聘1:招聘中)")
    private Integer status;*/
/*    @ApiModelProperty(value = "审核状态(0:审核中 1:审核失败 2:审核成功)")
    private Integer audit;*/
}
