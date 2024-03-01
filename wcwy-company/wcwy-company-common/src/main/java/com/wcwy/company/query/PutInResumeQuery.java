package com.wcwy.company.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * 投递简历表
 *
 * @TableName put_in_resume
 */
@Data
@ApiModel("投递简历表")
public class PutInResumeQuery extends PageQuery {
    /**
     * 投放简历id
     */
    @ApiModelProperty("投放简历id")
    private String putInResumeId;

    /**
     * 投放的岗位
     */
    @ApiModelProperty("投放的岗位")
    private String putInPost;

    /**
     * 投放的公司
     */
    @ApiModelProperty("投放的公司   注：查询子企业投简需要填")
    @NotBlank(message = "子公司不能为空")
    private String putInComppany;

    /**
     * 求职者
     */
    @ApiModelProperty("求职者")
    private String putInJobhunter;

    /**
     * 投放人
     */
    @ApiModelProperty("投放人    注：不用填")
    private String putInUser;


    /**
     * 投放状态(1:浏览、 2:下载、3排除  4:约面、5:面试中6淘汰、7:offer、8:入职、9:保证期)
     */
    @ApiModelProperty(" 投放状态(1:浏览、 2:下载、3排除  4:约面、5:面试中 6取消面试7淘汰、8:offer、9:入职、10:保证期)")
    private Integer resumeState;

    @ApiModelProperty("身份(1:企业2:投放人) 注：不用填")
    private Integer identity;

    /**
     * 是的代投(0:不是 1:是)
     */
    @ApiModelProperty("是的代投(0:求职者 1:推荐官)")
    private String easco;
}