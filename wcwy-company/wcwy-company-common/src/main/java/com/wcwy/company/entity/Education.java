package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 学历表
 * @TableName education
 */
@TableName(value ="education")
@Data
@ApiModel(value = "学历表接口")
public class Education implements Serializable {
    /**
     * 学历id
     */
    @TableId(value = "education_id")
    @ApiModelProperty(value = "学历id")
    private Integer educationId;

    /**
     * 学历
     */
    @TableField(value = "education_type")
    @ApiModelProperty(value = "学历")
    private String educationType;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @ApiModelProperty(value = "逻辑删除")
    private Integer deleted;

    /**
     * 创建人
     */
    @TableField(value = "creata_id")
    @ApiModelProperty(value = "创建人")
    private String creataId;

    /**
     * 创建时间
     */
    @TableField(value = "creata_time")
   // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creataTime;

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
        Education other = (Education) that;
        return (this.getEducationId() == null ? other.getEducationId() == null : this.getEducationId().equals(other.getEducationId()))
            && (this.getEducationType() == null ? other.getEducationType() == null : this.getEducationType().equals(other.getEducationType()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
            && (this.getCreataId() == null ? other.getCreataId() == null : this.getCreataId().equals(other.getCreataId()))
            && (this.getCreataTime() == null ? other.getCreataTime() == null : this.getCreataTime().equals(other.getCreataTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getEducationId() == null) ? 0 : getEducationId().hashCode());
        result = prime * result + ((getEducationType() == null) ? 0 : getEducationType().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        result = prime * result + ((getCreataId() == null) ? 0 : getCreataId().hashCode());
        result = prime * result + ((getCreataTime() == null) ? 0 : getCreataTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", educationId=").append(educationId);
        sb.append(", educationType=").append(educationType);
        sb.append(", deleted=").append(deleted);
        sb.append(", creataId=").append(creataId);
        sb.append(", creataTime=").append(creataTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}