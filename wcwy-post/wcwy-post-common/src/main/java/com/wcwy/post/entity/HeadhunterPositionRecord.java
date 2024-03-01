package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 猎头岗位金额记录表
 * @TableName headhunter_position_record
 */
@TableName(value ="headhunter_position_record")
@Data
@ApiModel("猎头岗位金额记录表")
public class HeadhunterPositionRecord implements Serializable {
    /**
     * 猎头职位费记录id
     */
    @TableId(value = "headhunter_position_record_id",type = IdType.AUTO)
    @ApiModelProperty(value = "猎头职位费记录id")
    private Integer headhunterPositionRecordId;

    /**
     * 月份
     */
    @TableField(value = "month")
    @ApiModelProperty(value = "月份")
    private Integer month;

    /**
     * 金额
     */
    @TableField(value = "money")
    @ApiModelProperty(value = "金额")
    private BigDecimal money;

    /**
     * 绑定的岗位
     */
    @TableField(value = "post_id")
    @ApiModelProperty(value = "绑定的岗位")
    private String postId;

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
        HeadhunterPositionRecord other = (HeadhunterPositionRecord) that;
        return (this.getHeadhunterPositionRecordId() == null ? other.getHeadhunterPositionRecordId() == null : this.getHeadhunterPositionRecordId().equals(other.getHeadhunterPositionRecordId()))
            && (this.getMonth() == null ? other.getMonth() == null : this.getMonth().equals(other.getMonth()))
            && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getHeadhunterPositionRecordId() == null) ? 0 : getHeadhunterPositionRecordId().hashCode());
        result = prime * result + ((getMonth() == null) ? 0 : getMonth().hashCode());
        result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", headhunterPositionRecordId=").append(headhunterPositionRecordId);
        sb.append(", month=").append(month);
        sb.append(", money=").append(money);
        sb.append(", postId=").append(postId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}