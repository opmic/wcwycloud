package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付兑换配置表
 *
 * @TableName t_pay_config
 */

@Data
@ApiModel("支付兑换配置修改实体类")
public class TPayConfigVO {
    /**
     * 主键Id
     */
    @ApiModelProperty("主键Id")
    @NotNull(message = "主键Id不能为空")
    private Integer id;

    /**
     * 人民币
     */

/*    @ApiModelProperty("人民币")
    private BigDecimal money;*/

    /**
     * 人民币
     */

   /* @ApiModelProperty("人民币")
    private Integer beanCount;
*/
    /**
     * 简历等级A(20w<=年薪<30w)
     */

    @ApiModelProperty("简历等级A(10万≥年薪)")
    @NotNull(message = "简历等级A的无忧币数量不能为空")
    private Integer gradeA;

    /**
     * 简历等级B(30w<=年薪<40w)
     */
    @ApiModelProperty("简历等级B(10万< 年薪 ≤ 20万)")
    @NotNull(message = "简历等级B的无忧币数量不能为空")
    private Integer gradeB;

    /**
     * 简历等级C(40w<=年薪<50w)
     */
    @ApiModelProperty("简历等级C(20万< 年薪 ≤ 30万)")
    @NotNull(message = "简历等级C的无忧币数量不能为空")
    private Integer gradeC;

    /**
     * 简历等级D(>=50w)
     */
    @ApiModelProperty("简历等级D(30万< 年薪 ≤ 50万)")
    @NotNull(message = "简历等级D的无忧币数量不能为空")
    private Integer gradeD;
    /**
     * 简历等级D(>=50w)
     */
    @ApiModelProperty("简历等级E(50万<年薪)")
    @NotNull(message = "简历等级E的无忧币数量不能为空")
    private Integer gradeE;


    /**
     * 充值
     */
/*

    private Integer rechargeCount;
*/
    @ApiModelProperty("折扣")
    @NotNull(message = "折扣不能为空")
    private Integer  discount;

}