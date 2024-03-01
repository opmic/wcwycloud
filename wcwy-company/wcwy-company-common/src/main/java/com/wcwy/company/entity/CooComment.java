package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 评论
 * @TableName coo_comment
 */
@TableName(value ="coo_comment")
@Data
@ApiModel("评论")
public class CooComment implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("评论id")
    private Long id;

    /**
     * 帖子id
     */
    @TableField(value = "coo_tribe_id")
    @ApiModelProperty("帖子id")
    private Long cooTribeId;

    /**
     * 回答
     */
    @TableField(value = "answer")
    @ApiModelProperty("回答")
    private String answer;

    /**
     * 省
     */
    @TableField(value = "province")
    @ApiModelProperty("省")
    private String province;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 评论人
     */
    @TableField(value = "create_user")
    @ApiModelProperty("评论人")
    private String createUser;

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
        CooComment other = (CooComment) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCooTribeId() == null ? other.getCooTribeId() == null : this.getCooTribeId().equals(other.getCooTribeId()))
            && (this.getAnswer() == null ? other.getAnswer() == null : this.getAnswer().equals(other.getAnswer()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCooTribeId() == null) ? 0 : getCooTribeId().hashCode());
        result = prime * result + ((getAnswer() == null) ? 0 : getAnswer().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", cooTribeId=").append(cooTribeId);
        sb.append(", answer=").append(answer);
        sb.append(", province=").append(province);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}