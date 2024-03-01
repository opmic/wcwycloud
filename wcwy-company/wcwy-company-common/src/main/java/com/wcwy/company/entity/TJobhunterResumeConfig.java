package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 隐藏简历配置表
 * @TableName t_jobhunter_resume_config
 */
@TableName(value ="t_jobhunter_resume_config")
@Data
@ApiModel(value = "隐藏简历配置表")
public class TJobhunterResumeConfig implements Serializable {
    /**
     * 主键Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键Id")
    private Long id;

    /**
     * 用户Id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户Id")
    private String userId;

    /**
     * 隐藏简历状态(0显示 1隐藏)
     */
    @TableField(value = "visible")
    @ApiModelProperty(value = "隐藏简历状态(0显示 1隐藏)")
    private Integer visible;

    /**
     * 对企业隐藏简历状态(0显示 1隐藏)
     */
    @TableField(value = "hunter_visible")
    @ApiModelProperty(value = "对企业隐藏简历状态(0显示 1隐藏)")
    private Integer hunterVisible;

    /**
     * 不看猎头职位状态(0可见 1不可见)
     */
    @TableField(value = "hunter_position_visible")
    @ApiModelProperty(value = " 不看猎头职位状态(0可见 1不可见)")
    private Integer hunterPositionVisible;

    /**
     * 不看外地职位(0可见 1不可见)
     */
    @TableField(value = "nonlocal_position_visible")
    @ApiModelProperty(value = "不看外地职位(0可见 1不可见)")
    private Integer nonlocalPositionVisible;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

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
        TJobhunterResumeConfig other = (TJobhunterResumeConfig) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getVisible() == null ? other.getVisible() == null : this.getVisible().equals(other.getVisible()))
            && (this.getHunterVisible() == null ? other.getHunterVisible() == null : this.getHunterVisible().equals(other.getHunterVisible()))
            && (this.getHunterPositionVisible() == null ? other.getHunterPositionVisible() == null : this.getHunterPositionVisible().equals(other.getHunterPositionVisible()))
            && (this.getNonlocalPositionVisible() == null ? other.getNonlocalPositionVisible() == null : this.getNonlocalPositionVisible().equals(other.getNonlocalPositionVisible()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getVisible() == null) ? 0 : getVisible().hashCode());
        result = prime * result + ((getHunterVisible() == null) ? 0 : getHunterVisible().hashCode());
        result = prime * result + ((getHunterPositionVisible() == null) ? 0 : getHunterPositionVisible().hashCode());
        result = prime * result + ((getNonlocalPositionVisible() == null) ? 0 : getNonlocalPositionVisible().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", visible=").append(visible);
        sb.append(", hunterVisible=").append(hunterVisible);
        sb.append(", hunterPositionVisible=").append(hunterPositionVisible);
        sb.append(", nonlocalPositionVisible=").append(nonlocalPositionVisible);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}