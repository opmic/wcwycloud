package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * ClassName: AuditContractVO
 * Description:
 * date: 2022/11/17 18:27
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("审核合同")
public class AuditContractVO {
    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    @NotBlank(message = "合同id不能为空")
    private String contractId;
    /**
     * 审核状态(0:审核中 1:审核成功 2:审核失败)
     */
    @ApiModelProperty("审核状态(0:审核中 1:审核成功 2:审核失败)")
    @NotNull(message = "审核状态不能为空")
    private Integer auditContract;

    /**
     * 失败原因
     */
    @TableField(value = "audit_cause")
    @ApiModelProperty("失败原因")
    private String auditCause;
}
