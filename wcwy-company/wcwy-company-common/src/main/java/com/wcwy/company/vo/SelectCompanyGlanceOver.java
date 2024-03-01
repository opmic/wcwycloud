package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 企业浏览表
 * @TableName company_glance_over
 */

@Data
@ApiModel("查询企业浏览")
public class SelectCompanyGlanceOver extends PageQuery {
    /**
     * 浏览表id
     */
    @ApiModelProperty("浏览表id")
    private String glanceOverId;

    /**
     * 浏览过的求职者
     */
    @ApiModelProperty("浏览过的求职者")
    private String glanceOverUserId;
    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    private String companyId;
}