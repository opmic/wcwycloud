package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.entity.TCompanyContractAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: TCompanyContractDTO
 * Description:
 * date: 2023/9/13 14:58
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("企业合同")
public class TCompanyContractDTO {
    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private String contractId;

    /**
     * 签约合同
     */
    @ApiModelProperty("签约合同")
    private List<String> signContract;

    /**
     * 合同有效期
     */
    @ApiModelProperty("合同有效期")
    private LocalDate contractDate;

    /**
     * 审核状态(0:审核中 1:审核成功 2:审核失败)
     */
    @ApiModelProperty("审核状态(0:审核中 1:审核成功 2:审核失败)")
    private Integer auditContract;

    /**
     * 申请状态(0:有效 1:已过期)
     */
    @ApiModelProperty("申请状态(0:有效 1:已过期)")
    private Integer state;
    /**
     * 审核时间
     */
    @ApiModelProperty("审核通过时间")
    private LocalDateTime auditTime;
    /**
     * 失败原因
     */
    @ApiModelProperty("失败原因")
    private String auditCause;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("服务产品")
    private String name;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("合同审核记录详情")
    List<TCompanyContractAudit> list;
}
