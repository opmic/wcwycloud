package com.wcwy.post.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: PostShareQuery
 * Description:
 * date: 2023/2/17 14:16
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Data
@ApiModel("职位中心")
public class PostShareQuery extends PageQuery {


    @ApiModelProperty(value = "企业Id")
    private String companyId;
    @ApiModelProperty(value = "岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位 4:简历付")
    private Integer postType;

    @ApiModelProperty(value = "岗位名称及工作城市")
    private String companyName;

    @ApiModelProperty(value = "岗位状态(0：停止招聘1:招聘中)")
    private Integer status;
    @ApiModelProperty(value = "审核状态(0:审核中 1:审核失败 2:审核成功)")
    private Integer audit;
}
