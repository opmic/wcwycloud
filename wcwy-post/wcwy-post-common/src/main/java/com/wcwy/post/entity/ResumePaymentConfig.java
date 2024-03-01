package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @TableName resume_payment_config
 */
@TableName(value ="resume_payment_config")
@Data
@ApiModel("简历付配置表")
public class ResumePaymentConfig implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    /**
     * 简历付类型(0:校园 1:职场)
     */
    @TableField(value = "type")
    @ApiModelProperty(" 简历付类型(0:校园 1:职场)")
    private Integer type;

    /**
     * 简历等级A(30万< 年薪)(大专)
     */
    @TableField(value = "grade_A")
    @ApiModelProperty("简历等级A(30万< 年薪)(大专)")
    private BigDecimal gradeA;

    /**
     * 简历等级D(30万≤ 年薪 ≤ 50万)(本科)
     */
    @TableField(value = "grade_B")
    @ApiModelProperty("简历等级D(30万≤ 年薪 ≤ 50万)(本科)")
    private BigDecimal gradeB;

    /**
     * 简历等级E(50万>年薪)(硕士)
     */
    @TableField(value = "grade_C")
    @ApiModelProperty("简历等级E(50万>年薪)(硕士)")
    private BigDecimal gradeC;

    /**
     * (博士)
     */
    @TableField(value = "grade_D")
    @ApiModelProperty(" (博士)")
    private BigDecimal gradeD;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ResumePaymentConfig other = (ResumePaymentConfig) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getGradeA() == null ? other.getGradeA() == null : this.getGradeA().equals(other.getGradeA()))
            && (this.getGradeB() == null ? other.getGradeB() == null : this.getGradeB().equals(other.getGradeB()))
            && (this.getGradeC() == null ? other.getGradeC() == null : this.getGradeC().equals(other.getGradeC()))
            && (this.getGradeD() == null ? other.getGradeD() == null : this.getGradeD().equals(other.getGradeD()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getGradeA() == null) ? 0 : getGradeA().hashCode());
        result = prime * result + ((getGradeB() == null) ? 0 : getGradeB().hashCode());
        result = prime * result + ((getGradeC() == null) ? 0 : getGradeC().hashCode());
        result = prime * result + ((getGradeD() == null) ? 0 : getGradeD().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", gradeA=").append(gradeA);
        sb.append(", gradeB=").append(gradeB);
        sb.append(", gradeC=").append(gradeC);
        sb.append(", gradeD=").append(gradeD);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}